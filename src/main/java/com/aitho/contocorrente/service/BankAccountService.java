package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.OpenAccountRequestDto;
import com.aitho.contocorrente.dto.response.BankAccountResponseDto;
import com.aitho.contocorrente.dto.response.TransactionResponseDto;

public interface BankAccountService {

    TransactionResponseDto doCredit(String username, Long bankAccountId, Double amount);
    TransactionResponseDto doDebit(String username,Long bankAccountId, Double amount);

    Double getBalance(String username,Long bankAccountId);

    BankAccountResponseDto openAccount(OpenAccountRequestDto requestDto);
}
