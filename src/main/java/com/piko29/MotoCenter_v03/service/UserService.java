package com.piko29.MotoCenter_v03.service;

import com.piko29.MotoCenter_v03.repository.UserRepository;
import com.piko29.MotoCenter_v03.repository.UserRoleRepository;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.dto.UserCredentialsDtoMapper;
import com.piko29.MotoCenter_v03.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.piko29.MotoCenter_v03.model.dto.UserCredentialsDto;
import com.piko29.MotoCenter_v03.model.dto.UserRegistrationDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }

    public List<String> findAllUserEmails() {
        return userRepository.findAllUsersByRoles_Name(USER_ROLE)
                .stream()
                .map(User::getEmail)
                .toList();
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        if (isCurrentUserAdmin()) {
            userRepository.deleteByEmail(email);
        }
    }

    @Transactional
    public void register(UserRegistrationDto registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        String passwordHash = passwordEncoder.encode(registration.getPassword());
        user.setPassword(passwordHash);
        Optional<UserRole> userRole = userRoleRepository.findByName(USER_ROLE);
        userRole.ifPresentOrElse(
                role -> user.getRoles().add(role),
                () -> {
                    throw new NoSuchElementException();
                }
        );
        userRepository.save(user);
    }

    private boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isCurrentUserLogged(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> (authority.getAuthority().equals("ROLE_USER"))
                                    || authority.getAuthority().equals("ROLE_ADMIN"));
    }


    @Transactional
    public void changeCurrentUserPassword(String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername).orElseThrow();
        String newPasswordHash = passwordEncoder.encode(newPassword);
        currentUser.setPassword(newPasswordHash);
    }
}
