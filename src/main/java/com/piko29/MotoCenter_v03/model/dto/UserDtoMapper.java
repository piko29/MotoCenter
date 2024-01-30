package com.piko29.MotoCenter_v03.model.dto;

import com.piko29.MotoCenter_v03.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserDtoMapper {
    UserDto map(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        return dto;
    }

    public User map(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        return user;
    }
}