package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.enums.OperationType;
import com.aitho.contocorrente.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @MockBean
    private TransactionService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("test GET /transactions/history/{accountId} success")
    void testGetHistorySuccess() throws Exception {
        Long customerId = 1L;
        Long bankAccountId = 1L;
        List<TransactionResponseDto> transactionsDtos = getTransactionResponseDtos(bankAccountId, customerId);
        doReturn(transactionsDtos).when(service).getLastMovements(bankAccountId);

        mockMvc.perform(get("/transactions/history/{accountId}", bankAccountId))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].amount", is(50.00)))
                .andExpect(jsonPath("$[0].operationType", is(OperationType.CREDIT.name())))
                .andExpect(jsonPath("$[0].dateTime", notNullValue()))
                .andExpect(jsonPath("$[0].bankAccountId", is(bankAccountId.intValue())))
                .andExpect(jsonPath("$[0].customerId", is(customerId.intValue())));
    }

    private static List<TransactionResponseDto> getTransactionResponseDtos(Long bankAccountId, Long customerId) {
        TransactionResponseDto transaction1 = new TransactionResponseDto(1L, 50.00, OperationType.CREDIT, new Date(), bankAccountId, customerId);
        TransactionResponseDto transaction2 = new TransactionResponseDto(2L, 150.00, OperationType.CREDIT, new Date(), bankAccountId, customerId);
        TransactionResponseDto transaction3 = new TransactionResponseDto(3L, 50.00, OperationType.DEBIT, new Date(), bankAccountId, customerId);
        TransactionResponseDto transaction4 = new TransactionResponseDto(4L, 20.00, OperationType.DEBIT, new Date(), bankAccountId, customerId);
        TransactionResponseDto transaction5 = new TransactionResponseDto(5L, 80.00, OperationType.CREDIT, new Date(), bankAccountId, customerId);

        return Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5);
    }

}
