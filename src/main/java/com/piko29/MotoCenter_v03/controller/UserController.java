package com.piko29.MotoCenter_v03.controller;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.dto.MessageDto;
import com.piko29.MotoCenter_v03.model.dto.MessageDtoMapper;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDto;
import com.piko29.MotoCenter_v03.model.dto.UserRegistrationDto;
import com.piko29.MotoCenter_v03.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Controller
@AllArgsConstructor
@RequestMapping("/user-panel")
public class UserController {
    private final UserService userService;
    private final MessageDtoMapper messageDto;

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
    String userPanelMessages(Model model, Long senderId) {
        List<MessageDto> allUserMessages = userService.getMessagesByUsername(userService.getNameFromContextHolder());
        model.addAttribute("allUserMessages", allUserMessages);
//10.01
        List<MessageDto> allSentMessages = userService.getMessagesSentByOwner();
        model.addAttribute("allSentMessages", allSentMessages);
//12.01 a
//        List<MessageDto> allFilteredMessages = userService.getMessagesFromSpecificUser();
//        model.addAttribute("allFilteredMessages", allFilteredMessages);
        Set<String> allSenders = userService.getMessageSenders();
        model.addAttribute("sender", allSenders);
//15.01
//        Set<String> sender = userService.getSenderList();
//        model.addAttribute("sender", sender);

        return "user-messages";
    }
    @GetMapping("/user-messages/{email}")
    String chatWithUser(Model model, @PathVariable String email){
        List<MessageDto> messagesFromOneUser = userService.chatWithUser(email);
        model.addAttribute("messagesFromOneUser", messagesFromOneUser);
        return "chat-with-user";
    }

    //05.01
    @GetMapping("/user-messages/{id}/delete")
    String deleteMessage(@PathVariable Long id){
        userService.deleteMessage(id);
        return "redirect:/user-panel/user-messages";
    }
    //08.01
    @GetMapping("/user-messages/{id}/answer")
    String motoProductMessageForm(@PathVariable Long id,  Model model, Message message){
//a        MotoProduct motoProduct = userService.findMotoProduct(id);
//a        model.addAttribute("motoProduct", motoProduct);

//        MotoProduct motoProduct = motoProductService.findMotoProduct(id);
//        model.addAttribute("motoProduct", motoProduct);
        Message messageDto = userService.findMessage(id);
        model.addAttribute("messageDto", messageDto);
        model.addAttribute("message", message);//reading from existing message

        System.out.println("reading answer message form");
        return "answer-message-form";
    }
    @PostMapping("/user-messages/{id}/answer")
    String motoProductMessage(@PathVariable Long id,Message dto){
        userService.answerMotoProductMessage(dto, id);
        //motoProductService.sendMotoProductMessage(messageDto,id);
        System.out.println("sending answer message working fine");
        return "redirect:/user-panel/user-messages";
    }


    /*
    @PathVariable is for parts of the path (i.e. /person/{id})
    @RequestParam is for the GET query parameters (i.e. /person?name="Bob").
    @RequestBody is for the actual body of a request.
     */


}
