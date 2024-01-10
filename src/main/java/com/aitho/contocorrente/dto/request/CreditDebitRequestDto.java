package com.aitho.contocorrente.dto.request;

import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
public class CreditDebitRequestDto {

    @NotBlank(message = "Invalid input")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 4, fraction = 2, message = "input invalido. Puoi versare/prelevare fino a un massimo di 9999,99")
    private BigDecimal amount;

    @Min(1)
    private Long bankAccountId;
}
