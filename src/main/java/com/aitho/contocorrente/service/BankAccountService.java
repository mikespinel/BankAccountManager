package com.aitho.contocorrente.service;

public interface BankAccountService {

    void doCredit(Long bankAccountId, Double amount);
    void doDebit(Long bankAccountId, Double amount);

    Double getBalance(Long bankAccountId);
}
