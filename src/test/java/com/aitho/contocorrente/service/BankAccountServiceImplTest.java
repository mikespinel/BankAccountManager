package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.OperationType;
import com.aitho.contocorrente.repository.BankAccountRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(value = MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository repository;

    @Mock
    private TransactionServiceImpl transactionService;

    @InjectMocks
    private BankAccountServiceImpl service;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Ignore
    @DisplayName("Test doCreditSuccess Success")
    void doCreditSuccessTest(){
        Double initialBalance = 1000.00;
        Double amount = 50.00;

        //Setup our mock repository
        Customer customer = new Customer(10L, "Giacomo", "Leopardi", "90082020638", "1234", new HashSet<>());
        BankAccount bankAccount = new BankAccount(7L, initialBalance, 10L, customer, new HashSet<>());
        doReturn(bankAccount).when(repository).getReferenceById(bankAccount.getId());
        doReturn(bankAccount).when(repository).save(bankAccount);
        doNothing().when(transactionService).create(bankAccount, amount, OperationType.CREDIT);

        //Execute service call
        service.doCredit("90082020638", 1L, amount);

        // Assert the response
        Assertions.assertEquals(initialBalance + amount, bankAccount.getBalance(), "The balance was not incremented");

    }
}
