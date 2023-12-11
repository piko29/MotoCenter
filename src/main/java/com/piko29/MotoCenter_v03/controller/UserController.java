package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDto;
import com.piko29.MotoCenter_v03.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/user-panel")
public class UserController {
    private final UserService userService;

    @GetMapping
    String userPanel(Model model) {
        List<String> allUserEmails = userService.findAllUserEmails();
        model.addAttribute("userEmails", allUserEmails);
        System.out.println(userService.getNameFromContextHolder());
        return "user-panel";
    }


//added 09.12
    @GetMapping("/user-products")
    String userPanelMotoProducts(Model model) {
    List<MotoProductDto> allOwnedMotoProducts = userService.getProductsByUsername(userService.getNameFromContextHolder());
    model.addAttribute("allOwnedMotoProducts", allOwnedMotoProducts);
    return "user-owned-products";
    }

}
