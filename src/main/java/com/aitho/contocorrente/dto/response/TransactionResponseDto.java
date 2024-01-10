package com.aitho.contocorrente.dto.response;

import com.aitho.contocorrente.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponseDto {

    private Long id;
    private Double amount;
    private OperationType operationType;
    private Date dateTime;
    Long bankAccountId;
    Long customerId;
}
