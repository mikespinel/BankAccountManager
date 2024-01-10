package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.BankAccountRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountServiceImplTest {

    @InjectMocks
    BankAccountServiceImpl bankAccountService;

    @Mock
    BankAccountRepository bankAccountRepository;

    @Mock
    TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Test doCreditSuccess Success")
    public void doCreditSuccessTest() {
        Double initialBalance = 1000.00;
        Double amount = 50.00;

        Customer customer = Customer.builder()
                .firstName("Giacomo")
                .lastName("Leopardi")
                .taxCode("90082020638")
                .username("giacomol")
                .email("giacomo@email.com")
                .password("123456789")
                .build();

        BankAccount bankAccount = new BankAccount(1L, initialBalance, 1L, customer, new HashSet<>());

        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.getReferenceById(any())).thenReturn(bankAccount);
        when(bankAccountRepository.save(any())).thenReturn(bankAccount);
        when(transactionService.create(any(), any(), any())).thenReturn(null);

        Double balance = bankAccountService.getBalance(customer.getUsername(), bankAccount.getId());

        assertEquals(1000.00, balance);
        bankAccountService.doCredit(customer.getUsername(), bankAccount.getId(), amount);
        balance = bankAccountService.getBalance(customer.getUsername(), bankAccount.getId());
        assertEquals(1050.00, balance);

    }

}
