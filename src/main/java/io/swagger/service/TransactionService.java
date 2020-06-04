package io.swagger.service;

import io.swagger.dao.TransactionRepository;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        Transaction newTransaction = transaction;
        transactionRepository.save(newTransaction);

        return newTransaction;
    }

    public List<Transaction> getTransactions(String iBan) {
        List<Transaction> transactionList = new ArrayList<>();

        transactionRepository.findAll().forEach( transaction -> {
            if (transaction.getSender().equals(iBan) || transaction.getReceiver().equals(iBan)) {
                transactionList.add(transaction);
            }
        });

        return transactionList;
    }

}