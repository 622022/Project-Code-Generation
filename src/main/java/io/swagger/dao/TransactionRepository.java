package io.swagger.dao;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM Transaction LIMIT :limit")
    Iterable<Transaction> getAllTransactionsLimit(@Param("limit") Integer limit);

    Iterable<Transaction> getTransactionsBySenderOrReceiver(String iBanSender, String iBanReceiver);
}