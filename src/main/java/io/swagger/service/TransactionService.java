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

    public List<Transaction> getTransactions(String iBan) {
        List<Transaction> transactionList = new ArrayList<>();

        if (accountRepository.existsById(iBan)) {
            logger.warning("Invalid IBAN provided. Account does not exist");
            throw new IllegalArgumentException("Invalid IBAN provided. Account does not exist");
        }

        transactionRepository.findAll().forEach(transaction -> {
            if (transaction.getSender().equals(iBan) || transaction.getReceiver().equals(iBan)) {
                transactionList.add(transaction);
            }
        });
        return transactionList;
    }

    public void createTransaction(Transaction transaction) {
        // retrieve accounts sender and receiver
        Account accountSender = accountRepository.getAccountByIBAN(transaction.getSender());
        Account accountReceiver = accountRepository.getAccountByIBAN(transaction.getReceiver());

        // check if accounts exist
        if (accountSender == null || accountReceiver == null) {
            logger.info("Accounts sender and receiver cannot be found");
            throw new IllegalArgumentException("Accounts sender and receiver cannot be found");
        }

        // one cannot directly transfer from a savings account to an account that is not of the same customer
        // one cannot directly transfer to a savings account from an account that is not from the same customer
        if (accountSender.getOwnerId() == accountReceiver.getOwnerId()) {
            if (!accountSender.getIBAN().equals(accountReceiver.getIBAN())) {
                makeTransaction(accountSender, accountReceiver, transaction);
            }
            else {
                logger.info("Accounts sender and receiver cannot be the same account");
                throw new IllegalArgumentException("Accounts sender and receiver cannot be the same account");
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

        if (balanceSender - transactionAmount >= absoluteLimitSender) {
            if (dayLimitSender > 0) {
                if (transactionAmount <= transactionLimitSender && transactionAmount > 0)
                {
                    accountSender.setAmount(balanceSender - transactionAmount); // withdraw amount from sender
                    accountReceiver.setAmount(accountReceiver.getAmount() + transactionAmount); // add amount to receiver
                    accountSender.setDayLimit(dayLimitSender - 1); // decrease day limit per transaction

                    transactionRepository.save(transaction);
                } else {
                    logger.info("Transaction amount must be more than 0 and cannot be higher than transaction limit: " + transactionLimitSender);
                    throw new IllegalArgumentException("Transaction amount must be more than 0 and cannot be higher than transaction limit: " + transactionLimitSender);
                }
            } else {
                logger.info("Account sender: " + accountSender.getIBAN() + " day limit cannot be surpassed");
                throw new IllegalArgumentException("Account sender: " + accountSender.getIBAN() + " day limit cannot be surpassed");
            }
        } else {
            logger.info("Account sender: " + accountSender.getIBAN() + " balance cannot become lower than absolute limit: " + absoluteLimitSender);
            throw new IllegalArgumentException("Account sender: " + accountSender.getIBAN() + " balance cannot become lower than absolute limit: " + absoluteLimitSender);
        }
    }

}
