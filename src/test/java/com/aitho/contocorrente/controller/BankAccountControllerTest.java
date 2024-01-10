package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.service.BankAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BankAccountControllerTest {

    @MockBean
    private BankAccountServiceImpl service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("test GET /bank-accounts/balance success")
    void testGetBalanceSuccess() throws Exception{
        Long bankAccountId = 15L;
        doReturn(5000.00).when(service).getBalance("danilos", bankAccountId);

        mockMvc.perform(get("/bank-accounts/balance")
                .param("bankAccountId", String.valueOf(bankAccountId))
                        .principal(new UsernamePasswordAuthenticationToken("danilos", "987654321"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", is(5000.00)));

    }
}
