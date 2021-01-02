package io.swagger.service;

import com.google.common.collect.Iterables;
import io.swagger.model.content.Account;
import io.swagger.utils.Filter;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

@SpringBootTest
@AutoConfigureMockMvc
class AccountServiceTest {
    @Autowired
    private AccountService service;

    private int limit = 6;
    private Filter filter = new Filter(limit, 10);


    @Test
    void numberOfAccountsReturningFromGetAllAccountsAreEqualToLimitFilter() throws Exception {
        long numberOfItemsInAccountList = StreamSupport.stream(service.getAllAccounts(filter).spliterator(), false).count();
        assertEquals(limit, numberOfItemsInAccountList);
    }

    @Test
    void getSpecificAccountReturnTheRightAccount() throws Exception {
        String accountIBAN = getAnAccountIban();
        Account account = service.getSpecificAccount(accountIBAN);
        assertEquals(accountIBAN,account.getIBAN());
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void editAccount() {
    }

    private String getAnAccountIban() throws Exception {
        Iterable<Account> accounts = service.getAllAccounts(filter);
        String accountIBAN = (Iterables.get(accounts,0)).getIBAN();
        return accountIBAN;
    }
}