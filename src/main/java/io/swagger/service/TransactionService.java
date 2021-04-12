package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.content.Role;
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
    private UserRepository userRepository;
    private AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
            throw new IllegalArgumentException("Accounts sender or receiver cannot be found");
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

        if (balanceSender - transactionAmount >= absoluteLimitSender) { // check if absolute limit is surpassed
            if (dayLimitSender > 0) { // check if day limit has been surpassed
                if (transactionAmount <= transactionLimitSender && transactionAmount > 0) { // check if transaction limit has not been reached

                    accountSender.setAmount(balanceSender - transactionAmount); // withdraw amount from sender
                    accountSender.setDayLimit(dayLimitSender - 1); // decrease day limit per transaction
                    accountRepository.save(accountSender); // update account sender

                    accountReceiver.setAmount(accountReceiver.getAmount() + transactionAmount); // add amount to receiver
                    accountRepository.save(accountReceiver); // update account sender

                    String receiverName = userRepository.findById(accountReceiver.getOwnerId()).get().getUsername();
                    Role performedBy = userRepository.findById(accountSender.getOwnerId()).get().getRole();

                    transaction.setReceiverName(receiverName);
                    transaction.setPerformedby(performedBy);
                    transactionRepository.save(transaction); // save transaction
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
