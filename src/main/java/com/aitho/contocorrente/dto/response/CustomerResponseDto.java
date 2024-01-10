package com.aitho.contocorrente.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CustomerResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String taxCode;
    private Set<Long> bankAccountsIds;

}
