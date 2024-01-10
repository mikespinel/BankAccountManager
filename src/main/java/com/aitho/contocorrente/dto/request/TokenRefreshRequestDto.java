package com.aitho.contocorrente.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRefreshRequestDto {

    @NotBlank
    private String refreshToken;

}
