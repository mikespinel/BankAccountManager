package com.aitho.contocorrente;

import com.aitho.contocorrente.model.Transaction;
import com.aitho.contocorrente.repository.TransactionRepository;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.List;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TransactionRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("transactions.yml")
    void testGetLastMovements() {
        List<Transaction> transactions = repository.getLastMovements(2L);
        Assertions.assertEquals(5, transactions.size(), "Expected 5 transactions in the database");
    }
}
