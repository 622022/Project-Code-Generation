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

        //this ensures that only the accounts that are within the repository are used otherwise they will be null
        AccountObject accountSender = accountRepository.getAccountObjectByIBAN(transaction.getSender());
        AccountObject accountReceiver = accountRepository.getAccountObjectByIBAN(transaction.getReceiver());

        //checking if the accounts even exist & checking if they're not the same accounts
        if(accountSender != null && accountReceiver != null && accountSender != accountReceiver)
        {
            //checking if the account from where the money is being sent or to sent is a savings account
            if(accountSender.getType() == AccountObject.TypeEnum.SAVING || accountReceiver.getType()== AccountObject.TypeEnum.SAVING)
            {
                // then checking if accounts is of the same customer
                if(accountSender.getOwnerId().equals(accountReceiver.getOwnerId()))
                {
                    return makeTransaction(accountSender,accountReceiver,transaction);
                }
            }
            else
            {
                return makeTransaction(accountSender,accountReceiver,transaction);
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

    private Transaction makeTransaction(AccountObject accountSender,AccountObject accountReceiver, Transaction transaction)
    {
        boolean successfulTransaction;
        successfulTransaction = accountSender.withdrawAmount(transaction.getAmount()); // withdraw from sender
        accountReceiver.insertAmount(transaction.getAmount()); // add funds to receiver account

        //check if transaction was successful
        if(successfulTransaction == true)
        {
            transactionRepository.save(transaction);
            accountService.editAccount(transaction.getSender(),accountSender);
            accountService.editAccount(transaction.getReceiver(),accountReceiver);

            System.out.println("==============test============");
            System.out.println(accountSender.toString());
            System.out.println(accountReceiver.toString());
            return transaction;
        }
        return null;
    }


}
