package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.ContocorrenteJpaConfig;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { ContocorrenteJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class BankAccountRepositoryTest {

    @Resource
    private BankAccountRepository repository;

    @Test
    public void givenBankAccount_whenSave_thenGetOk() {
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

        BankAccount bankAccount = new BankAccount(null, 3000.00, customer.getId(), customer, new HashSet<>());
        BankAccount bankAccount2 = new BankAccount(null, 4000.00, customer2.getId(), customer2, new HashSet<>());

        repository.save(bankAccount);
        repository.save(bankAccount2);

        BankAccount bankAccountAssert = repository.getReferenceById(1L);
        BankAccount bankAccountAssert2 = repository.getReferenceById(2L);

        assertEquals((Double)3000.00, bankAccountAssert.getBalance());
        assertEquals((Double)4000.00, bankAccountAssert2.getBalance());

    }

}