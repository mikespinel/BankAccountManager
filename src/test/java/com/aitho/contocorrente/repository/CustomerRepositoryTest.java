package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.ContocorrenteJpaConfig;
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
public class CustomerRepositoryTest {

    @Resource
    private CustomerRepository repository;

    @Test
    public void givenCustomer_whenSave_thenGetOk() {
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
        customer.setBankAccounts(new HashSet<>());
        customer2.setBankAccounts(new HashSet<>());
        repository.save(customer);
        repository.save(customer2);

        Customer customerAssert = repository.getReferenceById(1L);
        assertEquals("Clara", customerAssert.getFirstName());
    }

}