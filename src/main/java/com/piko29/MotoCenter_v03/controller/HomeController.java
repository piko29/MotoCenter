package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
class HomeController {
    private final UserService userService;

    @GetMapping("/")
    String home() {
        if (userService.isCurrentUserLogged()) {
            System.out.println(userService.isCurrentUserLogged());//to debug
            return "index";
        } else {
            System.out.println(userService.isCurrentUserLogged());
            return "login-form";
        }
    }

}
