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
    public void doCredit(String taxCode, Long bankAccountId, Double amount) {
        BankAccount bankAccount = repository.getReferenceById(bankAccountId);
        if(bankAccount.getCustomer().getTaxCode().equalsIgnoreCase(taxCode)){
            bankAccount.setBalance(bankAccount.getBalance() + amount);
            bankAccount = repository.save(bankAccount);
            transactionService.create(bankAccount, amount, OperationType.CREDIT);
        }else{
            throw new BankAccountException("Non hai l'autorizzazione per effettuare transazioni su questo conto.");
        }
    }

    @Override
    public void doDebit(String taxCode, Long bankAccountId, Double amount) {
        BankAccount bankAccount = repository.getReferenceById(bankAccountId);
        if(bankAccount.getCustomer().getTaxCode().equalsIgnoreCase(taxCode)){
            if(amount < 0 || bankAccount.getBalance() < amount){
                throw new BankAccountException("Impossibile prelevare la somma richiesta in quanto supera il saldo dispnibile");
            }
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            repository.save(bankAccount);
            transactionService.create(bankAccount, amount, OperationType.DEBIT);
        }else{
            throw new BankAccountException("Non hai l'autorizzazione per effettuare transazioni su questo conto.");
        }
    }

    @Override
    public Double getBalance(String taxCode, Long bankAccountId) {
        BankAccount bankAccount = repository.getReferenceById(bankAccountId);
        if(bankAccount.getCustomer().getTaxCode().equalsIgnoreCase(taxCode)){
            if(repository.existsById(bankAccountId)){
                return repository.getReferenceById(bankAccountId).getBalance();
            }else{
                throw new EntityNotFoundException("Conto corrente non trovato");
            }
        }else{
            throw new BankAccountException("Non hai l'autorizzazione per accedere a questo conto.");
        }
    }
}
