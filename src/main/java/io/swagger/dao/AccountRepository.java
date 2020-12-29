package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Iterable<Account> getAccountsByOwnerId(int ownerId);
    Iterable<Account> getAccountsByType(Account.TypeEnum type);
    Iterable<Account> getAccountsByStatus(Account.StatusEnum status);
    Account getAccountByIBAN(String iban);


}
