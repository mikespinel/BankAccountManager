package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.CreditDebitRequestDto;
import com.aitho.contocorrente.service.BankAccountService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService bankAccountService) {
        this.service = bankAccountService;
    }

    @PostMapping(value = "/credit")
    public ResponseEntity<Void> doCredit(@RequestBody CreditDebitRequestDto dto, Principal principal){
        service.doCredit(principal.getName(), dto.getBankAccountId(), dto.getAmount().doubleValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/debit")
    public ResponseEntity<Void> doDebit(@RequestBody CreditDebitRequestDto dto, Principal principal){
        service.doDebit(principal.getName(), dto.getBankAccountId(), dto.getAmount().doubleValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<Double> getBalance(@RequestParam Long bankAccountId, Principal principal){
        Double balance = service.getBalance(principal.getName(), bankAccountId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}
