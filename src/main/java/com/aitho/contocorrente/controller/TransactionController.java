package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.TransactionResultsDto;
import com.aitho.contocorrente.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping(value = "/history/{accountId}")
    public ResponseEntity<List<TransactionResultsDto>> history(@PathVariable Long accountId){
        List<TransactionResultsDto> transactions = service.getLastMovements(accountId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
