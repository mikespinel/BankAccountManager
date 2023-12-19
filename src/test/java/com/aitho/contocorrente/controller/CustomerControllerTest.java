package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.service.CustomerService;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
class CustomerControllerTest {

    @MockBean
    private CustomerService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("test GET /customers/bank-accounts/{customerId} success")
    void testGetBankAccountsSuccess() throws Exception{
        // Setup our mocked service
        Long customerId = 15L;
        doReturn(Lists.newArrayList(123L, 127L, 132L)).when(service).getBankAccountList(customerId);

        // Execute the GET request
        mockMvc.perform(get("/customers/bank-accounts/{customerId}", customerId))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate headers
                //FIXME header null
                //.andExpect(header().string(HttpHeaders.LOCATION, "/customers/bank-accounts/{customerId}"))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is(123)))
                .andExpect(jsonPath("$[1]", is(127)))
                .andExpect(jsonPath("$[2]", is(132)));

    }
}
