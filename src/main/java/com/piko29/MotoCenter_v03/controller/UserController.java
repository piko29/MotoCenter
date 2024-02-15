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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Controller
@AllArgsConstructor
@RequestMapping("/user-panel")
public class UserController {
    private final UserService userService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";

    @GetMapping
    String userPanel() {
        return "user-panel";
    }
    @GetMapping("/change-password")
    String changePassword(){
        return "change-password";
    }

    @GetMapping("/user-products")
    String userPanelMotoProducts(Model model) {
    List<MotoProductDto> allOwnedMotoProducts = userService.getProductsByUsername(userService.getNameFromContextHolder());
    model.addAttribute("allOwnedMotoProducts", allOwnedMotoProducts);
    return "user-owned-products";
    }


    @GetMapping("/user-products/{id}")
    String motoProductDetails(@PathVariable Long id, Model model){
        List<MotoProduct> allDetails = userService.getMotoProductById(id);
        model.addAttribute("motoDetails", allDetails);
        return "owned-moto-product-details";
    }

    @GetMapping("/user-products/{id}/delete")
    String deleteMotoProduct(@PathVariable Long id){
            userService.deleteMotoProduct(id);
            return "redirect:/user-panel/user-products";
    }

    @GetMapping("/add")
    String addMotoProductForm(Model model){
        MotoProductDto motoProduct = new MotoProductDto();
        model.addAttribute("motoProduct", motoProduct);
        System.out.println("reading add form ");
        return "add-moto-product-form";
    }

    @PostMapping("/add")
    String addMotoProduct(MotoProductDto motoProductDto, @RequestParam("img") MultipartFile file)
            throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        userService.saveMotoProduct(motoProductDto,file.getOriginalFilename());
        System.out.println("adding picture done - controller");
        return "redirect:/user-panel";
    }


    @GetMapping("/user-products/{id}/edit")
    String editMotoProductForm(Model model,@PathVariable Long id){
        MotoProduct motoProduct = userService.findMotoProduct(id);
        model.addAttribute("motoProduct", motoProduct);
        //12.02
        System.out.println(motoProduct.getImage());
        //
        return "edit-moto-product-form";
    }
    @PostMapping("/user-products/{id}/edit")
    String editMotoProduct(MotoProductDto motoProductDto, @PathVariable Long id, @RequestParam("img") MultipartFile file)
        throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        userService.editMotoProduct(motoProductDto,id, file.getOriginalFilename());
        return "redirect:/user-panel/user-products";
    }

    @GetMapping("/user-messages")
    String userPanelMessages(Model model) {
        Set<String> interactionEmails = userService.getMessageRecipientsAndSenders();
        System.out.println(userService.getMessageRecipientsAndSenders());
        model.addAttribute("interactionEmail", interactionEmails);

        return "user-messages";
    }
    @GetMapping("/user-messages/{email}")
    String chatWithUser(Model model, @PathVariable String email){
        List<MessageDto> messagesFromOneUser = userService.chatWithUser(email);
        model.addAttribute("messagesFromOneUser", messagesFromOneUser);

        return "chat-with-user";
    }

    @GetMapping("/user-messages/{email}/delete")
    String deleteChat(@PathVariable String email){
        userService.deleteChatWithUser(email);
        return "redirect:/user-panel/user-messages";
    }

    @GetMapping("/user-messages/{email}/{id}/delete")
    String deleteMessage(@PathVariable Long id,@PathVariable String email,Model model){
        List<MessageDto> messagesFromOneUser = userService.chatWithUser(email);
        model.addAttribute("messagesFromOneUser", messagesFromOneUser);
        userService.deleteMessage(id);
        return "redirect:/user-panel/user-messages/{email}";
    }

    @GetMapping("/user-messages/{email}/{id}/answer")
    String motoProductMessageForm(@PathVariable Long id,@PathVariable String email, Model model, Message message){

        List<MessageDto> messagesFromOneUser = userService.chatWithUser(email);
        model.addAttribute("messagesFromOneUser", messagesFromOneUser);

        Message messageDto = userService.findMessage(id);
        model.addAttribute("messageDto", messageDto);
        model.addAttribute("message", message);//reading from existing message

        return "answer-message-form";
    }
    @PostMapping("/user-messages/{email}/{id}/answer")
    String motoProductMessage(@PathVariable Long id,@PathVariable String email,Message dto){
        userService.chatWithUser(email);
        userService.answerMotoProductMessage(dto, id);

        return "redirect:/user-panel/user-messages/{email}";
    }

    @GetMapping("/sold-products")
    String userPanelSoldMotoProducts(Model model) {
        List<MotoProductDto> allSoldMotoProducts = userService.getSoldProductsByUsername
                (userService.getNameFromContextHolder());
        model.addAttribute("allSoldMotoProducts", allSoldMotoProducts);
        return "user-sold-products";
    }
    @GetMapping("/bought-products")
    String userPanelBoughtMotoProducts(Model model) {
        List<MotoProduct> allBoughtMotoProducts = userService.getBoughtProductsByUsername();
        model.addAttribute("allBoughtMotoProducts", allBoughtMotoProducts);
        return "user-bought-products";
    }

}
