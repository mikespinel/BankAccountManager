package com.aitho.contocorrente;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.repository.BankAccountRepository;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.assertj.core.util.Lists;
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
class BankAccountRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BankAccountRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("bank-accounts.yml")
    void testFindAll() {
        List<BankAccount> bankAccounts = Lists.newArrayList(repository.findAll());
        Assertions.assertEquals(3, bankAccounts.size(), "Expected 3 bank-accounts in the database");
    }
}
