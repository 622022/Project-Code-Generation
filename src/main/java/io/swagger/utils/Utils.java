package io.swagger.utils;

import io.swagger.model.content.Account;
import io.swagger.model.content.Transaction;
import io.swagger.model.content.User;

public class Utils {
    public boolean authorizeAccount(Account account, User user){
        if (account.getOwnerId() == user.getUserId()){
            return true;
        }
            return false;
    }
    public boolean authorizeTransaction(Transaction transaction,User user){
       if(transaction.getSender() == user.getUsername()){
           return true;
       }
           return false;

    }
    public boolean authorizeEdit(User editor , int editedID){
        if(editor.getUserId() == editedID) {
            return true;
        }

        return false;
    }

}
