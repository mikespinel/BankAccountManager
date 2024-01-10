package com.aitho.contocorrente.mapper;


import com.aitho.contocorrente.dto.response.BankAccountResponseDto;
import com.aitho.contocorrente.model.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(target = "customerResponseDto", ignore = true)

    BankAccountResponseDto toResponseDto(BankAccount bankAccount);


    @Mapping(target = "customerResponseDto", ignore = true)
    List<BankAccountResponseDto> toResponseDtos(List<BankAccount> bankAccounts);

}
