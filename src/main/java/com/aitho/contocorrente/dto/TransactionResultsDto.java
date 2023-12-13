package com.aitho.contocorrente.dto;

import com.aitho.contocorrente.model.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TransactionResultsDto {

    private Long id;
    private Double amount;
    private OperationType operationType;
    private Date dateTime;
    Long bankAccountId;
    Long customerId;
}
