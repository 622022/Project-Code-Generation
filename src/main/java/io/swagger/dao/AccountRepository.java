package io.swagger.dao;

import io.swagger.model.content.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM Account LIMIT :limit OFFSET :offset")
    Iterable<Account> GetAllLimit(@Param("limit") Integer limit, @Param("offset") Integer offset);

    Iterable<Account> getAccountsByOwnerId(int ownerId);

    Account getAccountByIBAN(String iban);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.owner_id = :ownerId LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountsByOwnerId(@Param("ownerId") int ownerId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.type = :accountType LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountsByType(@Param("accountType") Integer type, @Param("limit") Integer limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.status = :status LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountsByStatus(@Param("status") Integer status, @Param("limit") Integer limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.status = :status AND a.owner_id = :ownerId  LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountByOwnerIdAndStatusCustom(@Param("status") Integer status, @Param("ownerId") Integer ownerId, @Param("limit") Integer limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.type = :accountType AND a.owner_id = :ownerId  LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountByOwnerIdAndTypeCustom(@Param("accountType") Integer type, @Param("ownerId") Integer ownerId, @Param("limit") Integer limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.status = :status AND a.type = :accountType  LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountByStatusAndTypeCustom(@Param("status") Integer status, @Param("accountType") Integer accountType, @Param("limit") Integer limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM Account a WHERE a.status = :status AND a.type = :accountType AND a.owner_id = :ownerId LIMIT :limit OFFSET :offset")
    Iterable<Account> getAccountByOwnerIdAndStatusAndTypeCustom(@Param("ownerId") Integer ownerId, @Param("status") Integer status, @Param("accountType") Integer accountType, @Param("limit") Integer limit, @Param("offset") int offset);


}
