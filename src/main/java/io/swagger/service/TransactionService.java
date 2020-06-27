package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.model.AccountObject;
import io.swagger.model.Transaction;
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


    public Transaction createTransaction(Transaction transaction) {

        Transaction transaction1 = transaction;
        String transactionSenderIBAN =transaction.getSender();
        String transactionReceiverIBAN =transaction.getReceiver();
        AccountObject accountSender = accountRepository.getAccountObjectByIBAN(transactionSenderIBAN);
        AccountObject accountReceiver = accountRepository.getAccountObjectByIBAN(transactionReceiverIBAN);

        //checking if the accounts even exist
        if(accountSender != null && accountReceiver != null)
        {
           if(accountSender.getOwnerId().equals(accountReceiver.getOwnerId()))
           {
               if(accountSender.getStatus() != accountReceiver.getStatus())
               {
                   Double transactionLimit = accountSender.getTransactionLimit();
                   Double dailyLimit = accountSender.withdrawAmount(transaction.getAmount());
                   Double amount = accountSender.getAmount();
                   Double absoluteLimit = accountSender.getAbsolutelimit();

                   //checking if the user has enough funds to send money
                   if(amount > transaction.getAmount() )
                   {
                       //checking if the user does not exceed his daily transaction limit, per transaction limit and absolute limit
                       if(transactionLimit > transaction.getAmount() && dailyLimit>0 && transaction.getAmount() < absoluteLimit)
                       {
                           accountSender.setDayLimit(dailyLimit);
                           transactionRepository.save(transaction1);
                           accountService.editAccount(transactionSenderIBAN,accountSender);
                           return transaction1;

                       }
                   }
               }
           }

        }

        return null;

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
