package com.aitho.contocorrente;

import com.aitho.contocorrente.service.BankAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BankAccountControllerTest {

    @MockBean
    private BankAccountService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("test GET /bank-accounts/balance success")
    void testGetBalanceSuccess() throws Exception{
        // Setup our mocked service
        Long bankAccountId = 15L;
        doReturn(5000.00).when(service).getBalance(bankAccountId);

        // Execute the GET request
        mockMvc.perform(get("/bank-accounts/balance")
                .param("bankAccountId", String.valueOf(bankAccountId)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                //FIXME header null
                //.andExpect(header().string(HttpHeaders.LOCATION, "/bank-accounts/balance"))

                // Validate the returned fields
                .andExpect(jsonPath("$", is(5000.00)));

    }
}
