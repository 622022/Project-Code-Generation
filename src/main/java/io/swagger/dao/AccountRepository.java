package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Iterable<Account> getAccountObjectByOwnerId(int ownerId);
    Iterable<Account> getAccountObjectByType(Account.TypeEnum type);
    Iterable<Account> getAccountObjectByStatus(Account.StatusEnum status);
    Account getAccountObjectByIBAN(String iban);


}
