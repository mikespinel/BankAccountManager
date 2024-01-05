package com.aitho.contocorrente.service;

public interface BankAccountService {

    void doCredit(String taxCode, Long bankAccountId, Double amount);
    void doDebit(String taxCode,Long bankAccountId, Double amount);


    Double getBalance(String taxCode,Long bankAccountId);
}
