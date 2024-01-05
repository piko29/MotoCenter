package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.dto.MessageDto;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDto;
import com.piko29.MotoCenter_v03.model.dto.UserRegistrationDto;
import com.piko29.MotoCenter_v03.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    //details 14.12
    @GetMapping("/user-products/{id}")
    String motoProductDetails(@PathVariable Long id, Model model){
        List<MotoProduct> allDetails = userService.getMotoProductById(id);
        model.addAttribute("motoDetails", allDetails);
        return "owned-moto-product-details";
    }
    //delete MotoProduct 14.12
    @GetMapping("/user-products/{id}/delete")
    String deleteMotoProduct(@PathVariable Long id){
            userService.deleteMotoProduct(id);
            return "redirect:/user-panel/user-products";
    }

    //adding MotoProduct 20.12
    @GetMapping("/add")
    String addMotoProductForm(Model model){
        MotoProductDto motoProduct = new MotoProductDto();
        model.addAttribute("motoProduct", motoProduct);
        System.out.println("reading add form ");
        return "add-moto-product-form";
    }

    @PostMapping("/add")
    String addMotoProduct(MotoProductDto motoProductDto){
        userService.saveMotoProduct(motoProductDto);
        System.out.println("adding done - controller");
        return "redirect:/user-panel";
    }

    //edit motoproduct 23.12
    @GetMapping("/user-products/{id}/edit")
    String editMotoProductForm(Model model,@PathVariable Long id){
        MotoProduct motoProduct = userService.findMotoProduct(id);
        model.addAttribute("motoProduct", motoProduct);
        return "edit-moto-product-form";
    }
    @PostMapping("/user-products/{id}/edit")
    String editMotoProduct(MotoProductDto motoProductDto, @PathVariable Long id){
        userService.editMotoProduct(motoProductDto,id);
        return "redirect:/user-panel/user-products";
    }

    //04.01
    @GetMapping("/user-messages")
    String userPanelMessages(Model model) {
        List<MessageDto> allUserMessages = userService.getMessagesByUsername(userService.getNameFromContextHolder());
        model.addAttribute("allUserMessages", allUserMessages);
        return "user-messages";
    }

    //05.01
    @GetMapping("/user-messages/{id}/delete")
    String deleteMessage(@PathVariable Long id){
        userService.deleteMessage(id);
        return "redirect:/user-panel/user-messages";
    }



}
