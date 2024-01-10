package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bankAccountRepository")
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
