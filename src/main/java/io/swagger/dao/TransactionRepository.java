package io.swagger.dao;

import io.swagger.model.content.Account;
import io.swagger.model.content.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM Transaction WHERE sender LIKE :iBanSender OR receiver LIKE :iBanReceiver AND receiver_name LIKE %:receiverName% LIMIT :limit OFFSET :offset")
    Iterable<Transaction> getAllTransactions(@Param("iBanSender") String iBanSender, @Param("iBanReceiver") String iBanReceiver, @Param("receiverName") String receiverName, @Param("limit") Integer limit, @Param("offset") int offset);

}