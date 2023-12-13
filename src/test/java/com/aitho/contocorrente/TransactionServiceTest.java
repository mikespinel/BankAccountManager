package com.aitho.contocorrente;

import com.aitho.contocorrente.dto.TransactionResultsDto;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.OperationType;
import com.aitho.contocorrente.model.Transaction;
import com.aitho.contocorrente.repository.TransactionRepository;
import com.aitho.contocorrente.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService service;

    @MockBean
    private TransactionRepository repository;

    @Test
    @DisplayName("Test getLastMovements Success")
    void testGetLastMovementsSuccess(){
        // Setup our mock repository
        Long bankAccountId = 7L;
        Customer customer = new Customer(10L, "Giacomo", "Leopardi", "90082020638", new HashSet<>());
        BankAccount bankAccount = new BankAccount(bankAccountId, 1000.00, 10L, customer, new HashSet<>());
        Transaction transaction1 = new Transaction(1L, 50.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction2 = new Transaction(2L, 150.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction3 = new Transaction(3L, 50.00, OperationType.DEBIT, new Date(), bankAccount);
        Transaction transaction4 = new Transaction(4L, 20.00, OperationType.DEBIT, new Date(), bankAccount);
        Transaction transaction5 = new Transaction(5L, 80.00, OperationType.CREDIT, new Date(), bankAccount);
        //Transaction transaction6 = new Transaction(6L, 30.00, OperationType.DEBIT, new Date(), bankAccount);

        customer.getBankAccounts().add(bankAccount);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5);
        bankAccount.setTransactions(new HashSet<>(transactions));

        doReturn(transactions).when(repository).getLastMovements(bankAccountId);

        // Execute the service call
        List<TransactionResultsDto> transactionResultsDtos = service.getLastMovements(bankAccountId);

        // Assert the response
        //Assertions.assertTrue(returnedList.isPresent(), "Customer was not found");
        Assertions.assertNotNull(transactionResultsDtos, "Transactions was not found");
        Assertions.assertSame(5, transactionResultsDtos.size(), "The number of transaction expected is 5");

    }
}
