package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.request.CreditDebitRequestDto;
import com.aitho.contocorrente.dto.request.OpenAccountRequestDto;
import com.aitho.contocorrente.dto.response.BankAccountResponseDto;
import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService bankAccountService) {
        this.service = bankAccountService;
    }

    @PostMapping(value = "/credit")
    public ResponseEntity<TransactionResponseDto> doCredit(@RequestBody CreditDebitRequestDto dto, HttpServletRequest httpServletRequest) {
        TransactionResponseDto responseDto = service.doCredit(httpServletRequest.getUserPrincipal().getName(), dto.getBankAccountId(), dto.getAmount().doubleValue());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/debit")
    public ResponseEntity<TransactionResponseDto> doDebit(@RequestBody CreditDebitRequestDto dto, HttpServletRequest httpServletRequest) {
        TransactionResponseDto responseDto = service.doDebit(httpServletRequest.getUserPrincipal().getName(), dto.getBankAccountId(), dto.getAmount().doubleValue());
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<Double> getBalance(@RequestParam Long bankAccountId, HttpServletRequest httpServletRequest) {
        Double balance = service.getBalance(httpServletRequest.getUserPrincipal().getName(), bankAccountId);
        return ResponseEntity.ok().body(balance);
    }

    @PostMapping(value = "/open")
    public ResponseEntity<BankAccountResponseDto> openAccount(@RequestBody OpenAccountRequestDto dto) {
        BankAccountResponseDto responseDto = service.openAccount(dto);
        return ResponseEntity.ok().body(responseDto);
    }
}
