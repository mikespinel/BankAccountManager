package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.TransactionResultsDto;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.enums.OperationType;

import java.util.List;

public interface TransactionService {

    List<TransactionResultsDto> getLastMovements(Long bankAccountId);

    void create(BankAccount bankAccount, Double amount, OperationType operationType);
}
