package com.aitho.contocorrente.service;

import com.aitho.contocorrente.mapper.CustomerMapper;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.CustomerRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@ExtendWith(value = MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl service;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getBankAccountList Success")
    void testGetBankAccountList(){
        // Setup our mock repository
        Customer customer = new Customer(10L, "Giacomo", "Leopardi", "90082020638", "1234", new HashSet<>());
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
