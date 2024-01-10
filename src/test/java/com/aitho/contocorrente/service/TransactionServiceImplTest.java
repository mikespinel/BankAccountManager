package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.enums.OperationType;
import com.aitho.contocorrente.mapper.TransactionMapper;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.Transaction;
import com.aitho.contocorrente.repository.TransactionRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @Test
    @DisplayName("Test getLastMovements Success")
    public void testGetLastMovementsSuccess(){
        List<Transaction> transactions = getTransactionResponseDtos() ;
        doReturn(transactions).when(repository).getLastMovements(any());
        doReturn(getTransactionResponseDtos()).when(mapper).toResponseDtos(transactions);
        doReturn(toDto(transactions)).when(mapper).toResponseDtos(transactions);

        List<TransactionResponseDto> transactionResponseDtos = service.getLastMovements(1L);

        Assertions.assertNotNull(transactionResponseDtos, "Transactions was not found");
        Assertions.assertSame(5, transactionResponseDtos.size(), "The number of transaction expected is 5");

    }

    private static List<Transaction> getTransactionResponseDtos() {
        Customer customer = Customer.builder()
                .firstName("Clara")
                .lastName("Spinello")
                .taxCode("SPNCLR0200000")
                .username("claras")
                .email("clara@email.com")
                .password("123456789")
                .build();
        BankAccount bankAccount = new BankAccount(null, 3000.00, customer.getId(), customer, new HashSet<>());

        Transaction transaction1 = new Transaction(1L, 50.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction2 = new Transaction(2L, 150.00, OperationType.CREDIT, new Date(), bankAccount);
        Transaction transaction3 = new Transaction(3L, 50.00, OperationType.DEBIT, new Date(), bankAccount);
        Transaction transaction4 = new Transaction(4L, 20.00, OperationType.DEBIT, new Date(), bankAccount);
        Transaction transaction5 = new Transaction(5L, 80.00, OperationType.CREDIT, new Date(), bankAccount);

        return Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5);
    }

    private static List<TransactionResponseDto> toDto(List<Transaction> transactions) {
        List<TransactionResponseDto> resultsDtos = new ArrayList<>();
        transactions.forEach(t -> {
            resultsDtos.add(new TransactionResponseDto(
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
