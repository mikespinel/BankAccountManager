package com.aitho.contocorrente;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.OperationType;
import com.aitho.contocorrente.repository.BankAccountRepository;
import com.aitho.contocorrente.service.BankAccountService;
import com.aitho.contocorrente.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class BankAccountServiceTest {

    @Autowired
    private BankAccountService service;

    @MockBean
    private BankAccountRepository repository;

    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("Test doCreditSuccess Success")
    void doCreditSuccessTest(){
        Double initialBalance = 1000.00;
        Double amount = 50.00;

        //Setup our mock repository
        Customer customer = new Customer(10L, "Giacomo", "Leopardi", "90082020638", new HashSet<>());
        BankAccount bankAccount = new BankAccount(7L, initialBalance, 10L, customer, new HashSet<>());
        doReturn(bankAccount).when(repository).getReferenceById(7L);
        doReturn(bankAccount).when(repository).save(bankAccount);
        doNothing().when(transactionService).create(bankAccount, amount, OperationType.CREDIT);

        //Execute service call
        service.doCredit(7L, amount);

        // Assert the response
        Assertions.assertEquals(initialBalance + amount, bankAccount.getBalance(), "The balance was not incremented");

    }
}
