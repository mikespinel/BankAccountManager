package com.aitho.contocorrente.mapper;

import com.aitho.contocorrente.dto.response.TransactionResponseDto;
import com.aitho.contocorrente.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "bankAccountId", source = "bankAccount.id")
    @Mapping(target = "customerId", source = "bankAccount.customerId")
    TransactionResponseDto toResponseDto(Transaction transaction);

    @Mapping(target = "bankAccountId", source = "bankAccount.id")
    @Mapping(target = "customerId", source = "bankAccount.customerId")
    List<TransactionResponseDto> toResponseDtos(List<Transaction> transactions);
}
