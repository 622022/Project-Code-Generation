package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private AccountService accountService;
    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());


    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public Transaction createTransaction(Transaction transaction) {
        try {
            //this ensures that only the accounts that are within the repository are used otherwise they will be null
            Account accountSender = accountRepository.getAccountByIban(transaction.getSender());
            Account accountReceiver = accountRepository.getAccountByIban(transaction.getReceiver());

            //checking if the accounts even exist & checking if they're not the same accounts
            if (accountSender != null && accountReceiver != null && accountSender != accountReceiver) {
                //checking if the account from where the money is being sent or to sent is a savings account
                if (accountSender.getType() == Account.TypeEnum.SAVING || accountReceiver.getType() == Account.TypeEnum.SAVING) {
                    // then checking if accounts is of the same customer
                    if (accountSender.getOwnerId().equals(accountReceiver.getOwnerId())) {
                        return makeTransaction(accountSender, accountReceiver, transaction);
                    }
                } else {
                    return makeTransaction(accountSender, accountReceiver, transaction);
                }
            }
        } catch (Exception e) {
            logger.warning("Transaction not successful. Please check that the accounts exist and or your account limitations" + e.getMessage());
        }
        return null;
    }

    public List<Transaction> getTransactions(String iBan) {
        List<Transaction> transactionList = new ArrayList<>();

        transactionRepository.findAll().forEach(transaction -> {
            if (transaction.getSender().equals(iBan) || transaction.getReceiver().equals(iBan)) {
                transactionList.add(transaction);
            }
        });
        return transactionList;
    }

    public void createTransaction(Transaction transaction) {
        // retrieve accounts sender and receiver
        Account accountSender = accountRepository.getAccountByIban(transaction.getSender());
        Account accountReceiver = accountRepository.getAccountByIban(transaction.getReceiver());

        // check if accounts exist
        if (accountSender == null && accountReceiver == null) {
            logger.info("Accounts sender and receiver cannot be found");
            throw new IllegalArgumentException("Accounts sender and receiver cannot be found");
        }

        // one cannot directly transfer from a savings account to an account that is not of the same customer
        // one cannot directly transfer to a savings account from an account that is not from the same customer
        if (accountSender.getOwnerId() == accountReceiver.getOwnerId()) {
            if (accountSender.getType() != accountReceiver.getType()) {
                makeTransaction(accountSender, accountReceiver, transaction);
            }
            else {
                logger.info("Accounts sender and receiver cannot have the same account types");
                throw new IllegalArgumentException("Accounts sender and receiver cannot have the same account types");
            }
        }
        else {
            logger.info("Accounts sender and receiver cannot belong to different customers");
            throw new IllegalArgumentException("Accounts sender and receiver cannot belong to different customers");
        }
    }

    private void makeTransaction(Account accountSender, Account accountReceiver, Transaction transaction) {
        int dayLimitSender = accountSender.getDayLimit();
        double balanceSender = accountSender.getAmount();
        double absoluteLimitSender = accountSender.getAbsolutelimit();
        double transactionLimitSender = accountSender.getTransactionLimit();

        double transactionAmount = transaction.getAmount();

        if (balanceSender - transactionAmount > absoluteLimitSender) {
            if (dayLimitSender > 0) {
                if (transactionAmount < transactionLimitSender)
                {
                    accountSender.setAmount(balanceSender - transactionAmount); // withdraw amount from sender
                    accountReceiver.setAmount(accountReceiver.getAmount() + transactionAmount); // add amount to receiver
                    accountSender.setDayLimit(dayLimitSender - 1); // decrease day limit per transaction

                    transactionRepository.save(transaction);
                    System.out.println(accountSender);
                    System.out.println(accountReceiver);

                } else {
                    logger.info("Transaction amount cannot be higher than sender transaction limit");
                    throw new IllegalArgumentException("Transaction amount cannot be higher than sender transaction limit");
                }
            } else {
                logger.info("Account sender: " + accountSender.getIBAN() + " day limit cannot be surpassed");
                throw new IllegalArgumentException("Account sender: " + accountSender.getIBAN() + " day limit cannot be surpassed");
            }
        } else {
            logger.info("Account sender: " + accountSender.getIBAN() + " balance cannot become lower than absolute limit");
            throw new IllegalArgumentException("Account sender: " + accountSender.getIBAN() + " balance cannot become lower than absolute limit");
        }
    }

}
