package com.aitho.contocorrente.controller;

import com.aitho.contocorrente.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @GetMapping(value = "/bank-accounts/{customerId}")
    public ResponseEntity<List<Long>> getBankAccounts(@PathVariable Long customerId){
        List<Long> bankAccountsList = service.getBankAccountList(customerId);
        if(!bankAccountsList.isEmpty())
            return new ResponseEntity<>(bankAccountsList, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
