package com.aitho.contocorrente.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class OpenAccountRequestDto {

    @NotBlank(message = "Invalid input")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double credit;
    @NotBlank
    private Long customerId;

}
