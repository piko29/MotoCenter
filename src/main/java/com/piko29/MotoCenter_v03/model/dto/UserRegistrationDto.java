package com.piko29.MotoCenter_v03.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
