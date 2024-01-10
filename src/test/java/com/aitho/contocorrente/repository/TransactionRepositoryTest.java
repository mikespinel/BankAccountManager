package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.ContocorrenteJpaConfig;
import com.aitho.contocorrente.enums.OperationType;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { ContocorrenteJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class TransactionRepositoryTest {

    @Resource
    private TransactionRepository repository;

    @Test
    public void givenTransaction_whenSave_thenGetOk() {

        Customer customer = Customer.builder()
                .firstName("Clara")
                .lastName("Spinello")
                .taxCode("SPNCLR0200000")
                .username("claras")
                .email("clara@email.com")
                .password("123456789")
                .build();
        Customer customer2 = Customer.builder()
                .firstName("Danilo")
                .lastName("Spinello")
                .taxCode("SPNDNL9200000")
                .username("danilos")
                .email("danilo@email.com")
                .password("123456789")
                .build();

        customer.setId(1L);
        customer2.setId(2L);

        BankAccount bankAccount = new BankAccount(1L, 3000.00, customer.getId(), customer, new HashSet<>());
        BankAccount bankAccount2 = new BankAccount(2L, 4000.00, customer2.getId(), customer2, new HashSet<>());

        Transaction transaction = new Transaction(null, 50.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction2 = new Transaction(null, 20.00, OperationType.DEBIT, new Date(), bankAccount2);

        repository.save(transaction);
        repository.save(transaction2);

        Transaction transactionAssert = repository.getReferenceById(1L);
        Transaction transactionAssert2 = repository.getReferenceById(2L);

        assertEquals((Double)50.00, transactionAssert.getAmount());
        assertEquals((Double)20.00, transactionAssert2.getAmount());

    }

}