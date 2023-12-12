package com.aitho.contocorrente.dto;

import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.Transaction;
import lombok.Data;

import java.util.Set;

@Data
public class BankAccountResultsDto {

    private Long id;
    private Double balance;
    private Customer customer;
    private Set<Transaction> transactions;
}
