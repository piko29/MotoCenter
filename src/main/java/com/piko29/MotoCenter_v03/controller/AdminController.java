package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
class AdminController {
    private final UserService userService;

    @GetMapping
    String adminPanel(){
        return "admin-panel";
    }

    @GetMapping("/users-list")
    String usersList(Model model) {
        List<String> allUserEmails = userService.findAllUserEmails();
        model.addAttribute("userEmails", allUserEmails);
        return "users-management";
    }

    @GetMapping("/users-list/delete-user")
    String deleteUser(@RequestParam String email) {
        userService.deleteUserByEmail(email);
        return "redirect:/admin/users-list";
    }
}
