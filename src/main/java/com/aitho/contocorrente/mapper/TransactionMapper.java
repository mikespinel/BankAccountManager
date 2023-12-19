package com.aitho.contocorrente.mapper;

import com.aitho.contocorrente.dto.TransactionResultsDto;
import com.aitho.contocorrente.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /*@Mapping(target = "bankAccount.customer", ignore = true)
    @Mapping(target = "bankAccount.transactions", ignore = true)
    @Mapping(target = "bankAccount.balance", ignore = true)*/

    @Mapping(target = "bankAccountId", source = "bankAccount.id")
    @Mapping(target = "customerId", source = "bankAccount.customerId")
    TransactionResultsDto toResultsDto(Transaction transaction);

    @Mapping(target = "bankAccountId", source = "bankAccount.id")
    @Mapping(target = "customerId", source = "bankAccount.customerId")
    List<TransactionResultsDto> toResultsDtos(List<Transaction> transactions);
}
