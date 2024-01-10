package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Long> getBankAccountList(Long customerId);

    Customer getById(Long id);

    Customer getByUsername(String username);

    Optional<Customer> findById(long l);

    Customer save(Customer customer);
}
