package com.aitho.contocorrente.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountResponseDto {

    private Long id;
    private Double balance;
    private CustomerResponseDto customerResponseDto;

}
