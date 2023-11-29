package com.piko29.MotoCenter_v03.model.dto;

import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.UserRole;
import com.piko29.MotoCenter_v03.model.dto.UserCredentialsDto;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto map(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(email, password, roles);
    }
}
