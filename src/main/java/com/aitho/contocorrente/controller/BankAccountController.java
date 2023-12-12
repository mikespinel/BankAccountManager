package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.CreditDebitRequestDto;
import com.aitho.contocorrente.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService bankAccountService) {
        this.service = bankAccountService;
    }

    @PostMapping(value = "/credit")
    public ResponseEntity<Void> doCredit(@RequestBody CreditDebitRequestDto dto){
        service.doCredit(dto.getBankAccountId(), dto.getAmount().doubleValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/debit")
    public ResponseEntity<Void> doDebit(@RequestBody CreditDebitRequestDto dto){
        service.doDebit(dto.getBankAccountId(), dto.getAmount().doubleValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<Double> getBalance(@RequestParam Long bankAccountId){
        Double balance = service.getBalance(bankAccountId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}
