package com.aitho.contocorrente.dto;

import com.aitho.contocorrente.model.BankAccount;
import lombok.Data;

import java.util.Set;

@Data
public class CustomerResultsDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String taxCode;
    private Set<BankAccount> bankAccounts;

}
