package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.enums.OperationType;
import com.aitho.contocorrente.model.BankAccount;

import java.util.List;

public interface TransactionService {

    List<TransactionResponseDto> getLastMovements(Long bankAccountId);

    TransactionResponseDto create(BankAccount bankAccount, Double amount, OperationType operationType);
}
