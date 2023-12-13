package com.aitho.contocorrente.service;

import com.aitho.contocorrente.exception.BankAccountException;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.OperationType;
import com.aitho.contocorrente.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountRepository repository;
    private final TransactionService transactionService;

    public BankAccountServiceImpl(BankAccountRepository repository, TransactionService transactionService) {
        this.repository = repository;
        this.transactionService = transactionService;
    }

    @Override
    public void doCredit(Long bankAccountId, Double amount) {
        BankAccount bankAccount = repository.getReferenceById(bankAccountId);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccount = repository.save(bankAccount);
        transactionService.create(bankAccount, amount, OperationType.CREDIT);
    }

    @Override
    public void doDebit(Long bankAccountId, Double amount) {
        BankAccount bankAccount = repository.getReferenceById(bankAccountId);
        if(amount < 0 || bankAccount.getBalance() < amount){
            throw new BankAccountException("Impossibile prelevare la somma richiesta in quanto supera il saldo dispnibile");
        }
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        repository.save(bankAccount);
        transactionService.create(bankAccount, amount, OperationType.DEBIT);
    }

    @Override
    public Double getBalance(Long bankAccountId) {
        if(repository.existsById(bankAccountId)){
            return repository.getReferenceById(bankAccountId).getBalance();
        }else{
            throw new EntityNotFoundException("Conto corrente non trovato");
        }
    }
}
