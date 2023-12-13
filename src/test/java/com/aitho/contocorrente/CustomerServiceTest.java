package com.aitho.contocorrente;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.CustomerRepository;
import com.aitho.contocorrente.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    @Test
    @DisplayName("Test getBankAccountList Success")
    void testGetBankAccountList(){
        // Setup our mock repository
        Customer customer = new Customer(10L, "Giacomo", "Leopardi", "90082020638", new HashSet<>());
        BankAccount bankAccount = new BankAccount(7L, 1000.00, 10L, customer, new HashSet<>());
        customer.getBankAccounts().add(bankAccount);

        doReturn(true).when(repository).existsById(10L);
        doReturn(customer).when(repository).selectCustomerJoinBankAccount(10L);

        // Execute the service call
        List<Long> returnedList = service.getBankAccountList(10L);

        // Assert the response
        //Assertions.assertTrue(returnedList.isPresent(), "Customer was not found");
        Assertions.assertNotNull(returnedList, "Customer was not found");
        Assertions.assertSame(7L, returnedList.get(0), "The customer returned was not the same as the mock");
    }

}
