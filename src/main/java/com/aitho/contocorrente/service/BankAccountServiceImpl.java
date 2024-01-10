package com.aitho.contocorrente.service;

import com.aitho.contocorrente.dto.request.OpenAccountRequestDto;
import com.aitho.contocorrente.dto.response.BankAccountResponseDto;
import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.enums.OperationType;
import com.aitho.contocorrente.exception.BankAccountException;
import com.aitho.contocorrente.mapper.BankAccountMapper;
import com.aitho.contocorrente.mapper.CustomerMapper;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;
    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final BankAccountMapper bankAccountMapper;
    private final CustomerMapper customerMapper;

    private static final String BANK_ACCOUNT_ID_NOT_FOUND_MESSAGE = "Bank account with id %s not founded in db";


    public BankAccountServiceImpl(BankAccountRepository repository, TransactionService transactionService, CustomerService customerService, BankAccountMapper bankAccountMapper, CustomerMapper customerMapper) {
        this.repository = repository;
        this.transactionService = transactionService;
        this.customerService = customerService;
        this.bankAccountMapper = bankAccountMapper;
        this.customerMapper = customerMapper;
    }

    private BankAccount getById(Long id){
        log.info("Getting bank account with id {}", id);
        Optional<BankAccount> optionalBankAccount = repository.findById(id);
        if(!optionalBankAccount.isPresent()) {
            String message = String.format(BANK_ACCOUNT_ID_NOT_FOUND_MESSAGE, id);
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        return optionalBankAccount.get();
    }

    @Transactional
    @Override
    public TransactionResponseDto doCredit(String username, Long bankAccountId, Double amount) {
        log.info("Doing credit operation for username {} and bank account id {}", username, bankAccountId);
        BankAccount bankAccount = getById(bankAccountId);
        verifyOwner(bankAccount, username);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccount = repository.save(bankAccount);
        return transactionService.create(bankAccount, amount, OperationType.CREDIT);
    }

    @Transactional
    @Override
    public TransactionResponseDto doDebit(String username, Long bankAccountId, Double amount) {
        log.info("Doing debit operation for username {} and bank account id {}", username, bankAccountId);
        BankAccount bankAccount = getById(bankAccountId);
        verifyOwner(bankAccount, username);

        if (amount < 0 || bankAccount.getBalance() < amount){
            log.error("Debit operation failed: credit insufficient");
            throw new BankAccountException("Operazione fallita: credito insufficiente");
        }

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        repository.save(bankAccount);
        return transactionService.create(bankAccount, amount, OperationType.DEBIT);
    }

    @Override
    public Double getBalance(String username, Long bankAccountId) {
        log.info("Getting balance for bank account id {}", bankAccountId);
        BankAccount bankAccount = getById(bankAccountId);
        verifyOwner(bankAccount, username);

        return bankAccount.getBalance();
    }

    @Transactional
    @Override
    public BankAccountResponseDto openAccount(OpenAccountRequestDto requestDto) {
        log.info("Creating new bank account for customer with id {}", requestDto.getCustomerId());
        Customer customer = customerService.getById(requestDto.getCustomerId());
        BankAccount bankAccount = BankAccount.builder()
                .balance(requestDto.getCredit())
                .customerId(requestDto.getCustomerId())
                .customer(customer)
                .transactions(new HashSet<>())
                .build();
        BankAccountResponseDto responseDto = bankAccountMapper.toResponseDto(repository.save(bankAccount));
        responseDto.setCustomerResponseDto(customerMapper.toResponseDto(customer));
        return responseDto;
    }

    public void verifyOwner(BankAccount bankAccount, String username){
        if (!bankAccount.getCustomer().getUsername().equalsIgnoreCase(username)){
            log.error("Bank account with id {} not associated to customer {}", bankAccount, username);
            throw new BankAccountException("Conto corrente non associato all'utente.");
        }
    }
}
