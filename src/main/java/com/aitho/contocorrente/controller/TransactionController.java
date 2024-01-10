package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping(value = "/history/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> history(@PathVariable Long accountId){
        List<TransactionResponseDto> transactions = service.getLastMovements(accountId);
        return ResponseEntity.ok().body(transactions);
    }
}
