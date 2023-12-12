package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.TransactionResultsDto;
import com.aitho.contocorrente.mapper.TransactionMapper;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.OperationType;
import com.aitho.contocorrente.model.Transaction;
import com.aitho.contocorrente.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public TransactionServiceImpl(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TransactionResultsDto> getLastMovements(Long bankAccountId) {
        List<TransactionResultsDto> results = new ArrayList<>();
        List<Transaction> transactions = repository.getLastMovements(bankAccountId);
        transactions.forEach(t -> results.add(mapper.toResultsDto(t)));
        return results;
    }

    @Override
    public void create(BankAccount bankAccount, Double amount, OperationType operationType) {
        Transaction transaction = new Transaction();
        transaction.setBankAccount(bankAccount);
        transaction.setAmount(amount);
        transaction.setOperationType(operationType);
        transaction.setDateTime(new Date());
        repository.save(transaction);
    }
}
