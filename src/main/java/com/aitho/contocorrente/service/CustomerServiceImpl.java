package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.RoleEnum;
import com.aitho.contocorrente.repository.CustomerRepository;
import com.aitho.contocorrente.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_MESSAGE = "Utente con codice fiscale %s non trovato";


    public CustomerServiceImpl(CustomerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public Optional<Customer> findById(long l) {
        return repository.findById(l);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetailsImpl loadUserByUsername(String taxCode) throws UsernameNotFoundException {
        Customer customer = repository.selectCustomerByTaxCode(taxCode);
        if(customer == null) {
            String message = String.format(USER_NOT_FOUND_MESSAGE, taxCode);
            log.error(message);
            throw new UsernameNotFoundException(message);
        } else {
            log.debug("User found in the database: {}", taxCode);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name()));
            return UserDetailsImpl.build(customer);
        }
    }

    @Override
    public Customer save(Customer customer) {
        log.info("Saving user {} to the database", customer.getTaxCode());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return repository.save(customer);
    }
}
