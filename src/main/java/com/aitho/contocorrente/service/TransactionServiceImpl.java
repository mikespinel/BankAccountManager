package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.mapper.TransactionMapper;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.enums.OperationType;
import com.aitho.contocorrente.model.Transaction;
import com.aitho.contocorrente.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public TransactionServiceImpl(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TransactionResponseDto> getLastMovements(Long bankAccountId) {
        log.info("Getting last movements for bank account id {}", bankAccountId);
        List<Transaction> transactions = repository.getLastMovements(bankAccountId);
        return mapper.toResponseDtos(transactions);
    }

    @Override
    @Transactional
    public TransactionResponseDto create(BankAccount bankAccount, Double amount, OperationType operationType) {
        log.info("creating transaction for operation type {} for bank account {}", operationType.name(), bankAccount);
        Transaction transaction = new Transaction(null, amount, operationType, new Date(), bankAccount);
        return mapper.toResponseDto(repository.save(transaction));
    }
}
