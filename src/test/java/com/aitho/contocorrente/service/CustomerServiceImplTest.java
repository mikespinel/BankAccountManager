package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.CustomerRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository repository;

    @Test
    @DisplayName("Test getBankAccountList Success")
    public void testGetBankAccountList(){
        Customer customer = Customer.builder()
                .firstName("Giacomo")
                .lastName("Leopardi")
                .taxCode("90082020638")
                .username("giacomol")
                .email("giacomo@email.com")
                .password("123456789")
                .build();
        BankAccount bankAccount = new BankAccount(7L, 1000.00, 10L, customer, new HashSet<>());
        customer.setBankAccounts(new HashSet<>());
        customer.getBankAccounts().add(bankAccount);

        doReturn(true).when(repository).existsById(any());
        doReturn(customer).when(repository).selectCustomerJoinBankAccount(any());

        List<Long> returnedList = customerService.getBankAccountList(1L);

        Assertions.assertNotNull(returnedList, "Customer was not found");
        Assertions.assertSame(7L, returnedList.get(0), "The customer returned was not the same as the mock");
    }

}
