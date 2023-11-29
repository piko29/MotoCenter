package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.service.UserService;
import com.piko29.MotoCenter_v03.model.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
class RegistrationController {
    private final UserService userService;

    @GetMapping("/register")
    String registrationForm(Model model) {
        UserRegistrationDto user = new UserRegistrationDto();
        model.addAttribute("user", user);
        return "registration-form";
    }

    @PostMapping("/register")
    String register(UserRegistrationDto userRegistrationDto) {
        userService.register(userRegistrationDto);
        return "redirect:/confirmation";
    }

    @GetMapping("/confirmation")
    String registrationConfirmation() {
        return "registration-confirmation";
    }
}
