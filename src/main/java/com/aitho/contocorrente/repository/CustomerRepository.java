package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c inner join BankAccount b on b.customerId = c.id where c.id = :id")
    public Customer selectCustomerJoinBankAccount(@Param("id") Long id);

    @Query("select c from Customer c where c.taxCode = :taxCode")
    public Customer selectCustomerByTaxCode(@Param("taxCode") String taxCode);

    Optional<Customer> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByTaxCode(String taxCode);
}
