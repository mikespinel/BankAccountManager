package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.CustomerRepository;
import com.aitho.contocorrente.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;

    private static final String USERNAME_NOT_FOUND_MESSAGE = "Utente con username %s non trovato";
    private static final String USER_ID_NOT_FOUND_MESSAGE = "Utente con id %s non trovato";

    public CustomerServiceImpl(CustomerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Long> getBankAccountList(Long customerId) {
        log.info("Getting bank account ids for customer with id {}", customerId);
        List<Long> bankAccounts;

        if(!repository.existsById(customerId))
            throw new EntityNotFoundException(String.format(USER_ID_NOT_FOUND_MESSAGE, customerId));

        Customer customer = repository.selectCustomerJoinBankAccount(customerId);
        bankAccounts = customer.getBankAccounts().stream().map(BankAccount::getId).collect(Collectors.toList());

        return bankAccounts;

    }

    @Override
    public Customer getById(Long id){
        log.info("loading user with id {}", id);
        Optional<Customer> optionalCustomer = repository.findById(id);
        if(!optionalCustomer.isPresent()) {
            String message = String.format(USER_ID_NOT_FOUND_MESSAGE, id);
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        return optionalCustomer.get();
    }

    @Override
    public Customer getByUsername(String username){
        log.info("loading user with username {}", username);
        Optional<Customer> optionalCustomer = repository.findByUsername(username);
        if(!optionalCustomer.isPresent()) {
            String message = String.format(USERNAME_NOT_FOUND_MESSAGE, username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        }
        return optionalCustomer.get();
    }

    @Override
    public Optional<Customer> findById(long l) {
        return repository.findById(l);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = getByUsername(username);
        log.debug("User found in the database: {}", username);
        return UserDetailsImpl.build(customer);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        log.info("Saving user {} to the database", customer.getUsername());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return repository.save(customer);
    }
}
