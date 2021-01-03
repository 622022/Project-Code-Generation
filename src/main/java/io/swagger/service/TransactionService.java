package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.utils.Filter;
import io.swagger.model.content.Account;
import io.swagger.model.content.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private AccountService accountService;


    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public List<Transaction> getTransactions(Filter filter) {
        List<Transaction> transactionList = new ArrayList<>();

        if (!accountRepository.existsById(filter.iBan)) {
            throw new IllegalArgumentException("Invalid IBAN provided. Account does not exist");
        }

        transactionRepository.getAllTransactions(filter.iBan, filter.iBan, filter.receiverName,
                filter.limit, filter.offset).forEach(transactionList::add);

        return transactionList;
    }

    public void createTransaction(Transaction transaction) {
        // retrieve accounts sender and receiver
        Account accountSender = accountRepository.getAccountByIBAN(transaction.getSender());
        Account accountReceiver = accountRepository.getAccountByIBAN(transaction.getReceiver());

        // check if accounts exist
        if (accountSender == null || accountReceiver == null) {
            throw new IllegalArgumentException("Accounts sender and receiver cannot be found");
        }

        // one cannot directly transfer from a savings account to an account that is not of the same customer
        // one cannot directly transfer to a savings account from an account that is not from the same customer
        if (accountSender.getOwnerId() == accountReceiver.getOwnerId()) {
            if (!accountSender.getIBAN().equals(accountReceiver.getIBAN())) {
                makeTransaction(accountSender, accountReceiver, transaction);
            } else {
                throw new IllegalArgumentException("Accounts sender and receiver cannot be the same account");
            }
        } else {
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
                if (transactionAmount <= transactionLimitSender && transactionAmount > 0) {
                    accountSender.setAmount(balanceSender - transactionAmount); // withdraw amount from sender
                    accountReceiver.setAmount(accountReceiver.getAmount() + transactionAmount); // add amount to receiver
                    accountSender.setDayLimit(dayLimitSender - 1); // decrease day limit per transaction

                    transactionRepository.save(transaction);
                } else {
                    throw new IllegalArgumentException("Transaction amount must be more than 0 and cannot be higher than transaction limit: " + transactionLimitSender);
                }
            } else {
                throw new IllegalArgumentException("Account sender: " + accountSender.getIBAN() + " day limit cannot be surpassed");
            }
        } else {
            throw new IllegalArgumentException("Account sender: " + accountSender.getIBAN() + " balance cannot become lower than absolute limit: " + absoluteLimitSender);
        }
    }
}
