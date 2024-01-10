package com.aitho.contocorrente.mapper;


import com.aitho.contocorrente.dto.request.SignupRequestDto;
import com.aitho.contocorrente.dto.response.CustomerResponseDto;
import com.aitho.contocorrente.model.BankAccount;
import com.aitho.contocorrente.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "bankAccountsIds", source = "bankAccounts", qualifiedByName = "bankAccountsToIds")
    CustomerResponseDto toResponseDto(Customer customer);

    @Mapping(target = "bankAccountsIds", source = "bankAccounts", qualifiedByName = "bankAccountsToIds")
    List<CustomerResponseDto> toResponseDtos(List<Customer> customers);

    @Named(value = "bankAccountsToIds")
    static Set<Long> bankAccountsToIds(Set<BankAccount> bankAccounts) {
        if (bankAccounts == null)
            return new HashSet<>();
        return bankAccounts.stream().map(BankAccount::getId).collect(Collectors.toSet());
    }

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    Customer signUpRequestDtoToCustomer(SignupRequestDto signupRequestDto);
}
