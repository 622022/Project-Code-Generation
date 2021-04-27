package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.content.Role;
import io.swagger.utils.Filter;
import io.swagger.model.content.Account;
import io.swagger.model.content.Role;
import io.swagger.model.content.Transaction;
import io.swagger.model.content.User;
import io.swagger.utils.Filter;
import io.swagger.utils.JwtTokenUtil;
import io.swagger.utils.Utils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountService accountService;
    private UserRepository userRepository;
    private JwtTokenUtil jwtUtil;
    private Utils utils = new Utils();


    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountService accountService, UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.userRepository = userRepository;
        this.jwtUtil = new JwtTokenUtil(userRepository);
    }

    public List<Transaction> getTransactions(Filter filter, String token) {
        List<Transaction> transactionList = new ArrayList<>();

        if (!accountRepository.existsById(filter.iBan)) {
            throw new IllegalArgumentException("Invalid IBAN provided. Account does not exist");
        }
        User userPerformingAction = jwtUtil.getUserFromToken(token);
        Role userRole = jwtUtil.getRoleFromToken(token);
        Account userAccount = accountRepository.getAccountByIBAN(filter.iBan);
        if (userRole == Role.EMPLOYEE){
            transactionRepository.getAllTransactions(filter.iBan, filter.iBan, filter.receiverName,
                    filter.limit, filter.offset).forEach(transactionList::add);
        } else if (userRole == Role.CUSTOMER){
            if (utils.authorizeAccount(userAccount,userPerformingAction)){
                transactionRepository.getAllTransactions(filter.iBan, filter.iBan, filter.receiverName,
                        filter.limit, filter.offset).forEach(transactionList::add);
            } else {
                throw new SecurityException("Can not access transactions of the provided IBAN");
            }
        }
        return transactionList;
    }

    public void createTransaction(Transaction transaction,String token) {
        // retrieve accounts sender and receiver
        Account senderAccount = accountRepository.getAccountByIBAN(transaction.getSender());
        Account receiverAccount = accountRepository.getAccountByIBAN(transaction.getReceiver());
        User userPerformingAction = jwtUtil.getUserFromToken(token);
        Role userRole = jwtUtil.getRoleFromToken(token);
        // check if accounts exist
        if (senderAccount == null || receiverAccount == null) {
            throw new IllegalArgumentException("Accounts sender and receiver cannot be found");
        }
        if ((userRole == Role.EMPLOYEE) && checkIfTransactionIsPossible(senderAccount,receiverAccount) ){
            makeTransaction(senderAccount, receiverAccount, transaction);
        }
        else if(userRole == Role.CUSTOMER){
            if (utils.authorizeAccount(senderAccount,userPerformingAction) && checkIfTransactionIsPossible(senderAccount,receiverAccount)){
                makeTransaction(senderAccount, receiverAccount, transaction);
            }
            else {
                throw new SecurityException("Unauthorized to perform transaction");
            }
        }
    }

    private boolean checkIfTransactionIsPossible(Account sender, Account receiver){
        //check if senderAccount is saving can only send to his own checking account
        if ((sender.getType() == Account.TypeEnum.SAVING) || (receiver.getType() == Account.TypeEnum.SAVING) ){
            // one cannot directly transfer from a savings account to an account that is not of the same customer
            if (sender.getOwnerId() == receiver.getOwnerId()) {
                if (!sender.getIBAN().equals(receiver.getIBAN())) {
                    return true;
                } else {
                    throw new IllegalArgumentException("Accounts sender and receiver cannot be the same account");
                }
            } else {
                throw new IllegalArgumentException("Saving Accounts sender or receiver cannot belong to different customers");
            }
        }
        //if senderAccount is Checking, can send money to other checking account or his own saving account
        else if(sender.getType() == Account.TypeEnum.CHECKING){
            if (!sender.getIBAN().equals(receiver.getIBAN())) {
                return true;
            } else {
                throw new IllegalArgumentException("Accounts sender and receiver cannot be the same account");
            }
        }
        return false;
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
