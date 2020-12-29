package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM Account LIMIT :limit")
    Iterable<Account> GetAllLimit(@Param("limit") Integer limit);
    Iterable<Account> getAccountsByOwnerId(int ownerId);
    Iterable<Account> getAccountsByType(Account.TypeEnum type);
    Iterable<Account> getAccountsByStatus(Account.StatusEnum status);
    Account getAccountByIBAN(String iban);


}
