package com.aitho.contocorrente.service;

public interface BankAccountService {

    void doCredit(String username, Long bankAccountId, Double amount);
    void doDebit(String username,Long bankAccountId, Double amount);


    Double getBalance(String username,Long bankAccountId);
}
