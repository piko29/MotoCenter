package com.piko29.MotoCenter_v03.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserCredentialsDto {
    private final String email;
    private final String password;
    private final Set<String> roles;
}
