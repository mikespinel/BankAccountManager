package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("transactionRepository")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "select * from transaction t where t.bank_account_id = :bankAccountId order by t.date_time desc limit 5", nativeQuery = true)
    List<Transaction> getLastMovements(Long bankAccountId);

}
