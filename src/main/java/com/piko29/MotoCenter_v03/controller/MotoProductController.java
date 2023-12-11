package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDto;
import com.piko29.MotoCenter_v03.model.dto.UserRegistrationDto;
import com.piko29.MotoCenter_v03.service.MessageService;
import com.piko29.MotoCenter_v03.service.MotoProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    //added 06.12 not working yet
    private final MessageService messageService;

    @GetMapping
    String productPanel(Model model){
        List<MotoProduct> allProducts = motoProductService.findAllMotoProducts();
        model.addAttribute("motoProducts", allProducts);
        return "moto-product-panel";
    }

    @GetMapping("/{id}")//reading
    String motoProductDetails(@PathVariable Long id, Model model){
        List<MotoProduct> allDetails = motoProductService.getMotoProductById(id);
        model.addAttribute("motoDetails", allDetails);
        return "moto-product-details";
    }
    //added 06.12 to write a message to the owner, not finished
    @GetMapping("/{id}/message")
    String motoProductMessage(@PathVariable Long id, Model model){

        return "moto-message-form";
    }

}
