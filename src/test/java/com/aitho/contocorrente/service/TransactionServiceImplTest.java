package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.TransactionResultsDto;
import com.aitho.contocorrente.mapper.TransactionMapper;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.OperationType;
import com.aitho.contocorrente.model.Transaction;
import com.aitho.contocorrente.repository.TransactionRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.doReturn;

@ExtendWith(value = MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionServiceImpl service;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getLastMovements Success")
    void testGetLastMovementsSuccess(){
        // Setup our mock repository
        Long bankAccountId = 7L;
        Long customerId = 10L;
        List<Transaction> transactions = getTransactionResultsDtos() ;
        doReturn(transactions).when(repository).getLastMovements(bankAccountId);
        //doReturn(getTransactionResultsDtos()).when(mapper).toResultsDtos();
        doReturn(toDto(transactions)).when(mapper).toResultsDtos(transactions);

        // Execute the service call
        List<TransactionResultsDto> transactionResultsDtos = service.getLastMovements(bankAccountId);

        // Assert the response
        Assertions.assertNotNull(transactionResultsDtos, "Transactions was not found");
        Assertions.assertSame(5, transactionResultsDtos.size(), "The number of transaction expected is 5");

    }

    private static List<Transaction> getTransactionResultsDtos() {
        Customer customer = new Customer(1L, "Clara", "Spinello", "SPNCLR0200000", new HashSet<>());

        BankAccount bankAccount = new BankAccount(null, 3000.00, customer.getId(), customer, new HashSet<>());

        Transaction transaction1 = new Transaction(1L, 50.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction2 = new Transaction(2L, 150.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction3 = new Transaction(3L, 50.00, OperationType.DEBIT, new Date(), bankAccount);
        Transaction transaction4 = new Transaction(4L, 20.00, OperationType.DEBIT, new Date(), bankAccount);
        Transaction transaction5 = new Transaction(5L, 80.00, OperationType.CREDIT, new Date(), bankAccount);

        return Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5);
    }

    private static List<TransactionResultsDto> toDto(List<Transaction> transactions) {
        List<TransactionResultsDto> resultsDtos = new ArrayList<>();
        transactions.forEach(t -> {
            resultsDtos.add(new TransactionResultsDto(
                    t.getId(),
                    t.getAmount(),
                    t.getOperationType(),
                    t.getDateTime(),
                    t.getBankAccount().getId(),
                    t.getBankAccount().getCustomerId()));
        });
        return resultsDtos;
    }
}
