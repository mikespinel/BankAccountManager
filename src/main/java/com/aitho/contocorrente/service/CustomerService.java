package com.aitho.contocorrente.service;

import com.aitho.contocorrente.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Long> getBankAccountList(Long customerId);

    Optional<Customer> findById(long l);
}
