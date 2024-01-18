package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.service.MotoProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class MotoProductController {
    private final MotoProductService motoProductService;


    @GetMapping
    String productPanel(Model model){
        List<MotoProduct> allProducts = motoProductService.findAllMotoProducts();
        model.addAttribute("motoProducts", allProducts);
        return "moto-product-panel";
    }

    @GetMapping("/{id}")//maybe change this to findMotoProduct method
    String motoProductDetails(@PathVariable Long id, Model model){
        List<MotoProduct> allDetails = motoProductService.getMotoProductById(id);
        model.addAttribute("motoDetails", allDetails);
        return "moto-product-details";
    }
    //added 30.12 to write a message to the owner
    @GetMapping("/{id}/message")
    String motoProductMessageForm(@PathVariable Long id, Message messageDto, Model model){
        MotoProduct motoProduct = motoProductService.findMotoProduct(id);
        model.addAttribute("motoProduct", motoProduct);
        model.addAttribute("messageDto", messageDto);
        System.out.println("reading message form");
        return "moto-message-form";
    }
    @PostMapping("/{id}/message")
    String motoProductMessage(@PathVariable Long id, Message messageDto){
        motoProductService.sendMotoProductMessage(messageDto,id);
        System.out.println("sending message working fine");
        return "redirect:/products";
    }


}
