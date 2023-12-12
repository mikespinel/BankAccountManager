package com.aitho.contocorrente.exception;

public class BankAccountException extends RuntimeException{

    public BankAccountException(String errorMessage){
        super(errorMessage);
    }
}
