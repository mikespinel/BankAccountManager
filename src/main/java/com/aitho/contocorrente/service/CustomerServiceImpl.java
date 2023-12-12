package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Long> getBankAccountList(Long customerId) {
        List<Long> bankAccounts;
        if(repository.existsById(customerId)){
            //funziona ma durante il debug i valori sono null, ma se fai un evaluate del getter del campo il valore c'e'
            //Customer customer = repository.getReferenceById(customerId);

            Customer customer = repository.selectCustomerJoinBankAccount(customerId);
            bankAccounts = customer.getBankAccounts().stream().map(BankAccount::getId).collect(Collectors.toList());
            return bankAccounts;
        }else{
            throw new EntityNotFoundException("Impossibile trovare utente con id " + customerId);
        }
    }
}
