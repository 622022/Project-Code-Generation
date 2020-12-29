package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Iterable<Account> getAccountObjectByOwnerId(int ownerId);
    Iterable<Account> getAccountObjectByType(Account.TypeEnum type);
    Iterable<Account> getAccountObjectByStatus(Account.StatusEnum status);
    Account getAccountObjectByIBAN(String iban);
    @Query(nativeQuery = true, value = "SELECT * FROM Account LIMIT :limit")
    Iterable<Account> GetAllLimit(@Param("limit") Integer limit);


}
