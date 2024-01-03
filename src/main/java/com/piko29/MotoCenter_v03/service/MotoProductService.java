package com.piko29.MotoCenter_v03.service;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDto;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDtoMapper;
import com.piko29.MotoCenter_v03.repository.MessageRepository;
import com.piko29.MotoCenter_v03.repository.MotoProductRepository;
import com.piko29.MotoCenter_v03.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MotoProductService {
    private final MotoProductRepository motoProductRepository;
    private final MotoProductDtoMapper motoProductDtoMapper;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public List<MotoProduct> findAllMotoProducts() {
        return motoProductRepository.findAll().stream()
                .toList();
    }

    public List<MotoProduct> getMotoProductById(Long id) {
        return motoProductRepository.findById(id).stream().toList();
    }
    @Transactional
    public MotoProduct findMotoProduct(Long id){
        return motoProductRepository.findById(id).orElseThrow();
    }

    //private message 29.12
    /*
    -tworzenie formularza wiadomości, można przypisać do motoproduct?
    -pobieranie loginu użytkownika-sendera z getcontextholder
    -pobieranie właściciela z ogłoszenia
    -wpisywanie treści wiadomości w formularzu(większe okienko)
    -wysyłanie za pomocą przycisku metodą post
    -w panelu użytkownika przycisk z zakładką prywatne wiadomości
    -wyświtelanie wszystkich wiadomości (np. w liście)
     */
    public String getNameFromContextHolder() {

        return SecurityContextHolder.getContext()
                .getAuthentication().getName();

    }

    @Transactional
    public void sendMotoProductMessage(Message messageDto,Long id){
        Message message = new Message();
        User sender = userRepository.findByEmail(getNameFromContextHolder()).orElseThrow();//important line
        MotoProduct motoProduct = motoProductRepository.findById(id).orElseThrow();
        System.out.println(sender);//debug
        message.setSender(sender);
        System.out.println("Product id:"+ id);//debug
        message.setProductId(id);
        System.out.println(motoProduct.getTitle());
        message.setTitle(motoProduct.getTitle());
        System.out.println("recipient: " + motoProduct.getUser());
        message.setRecipient(motoProduct.getUser());
        message.setContent(messageDto.getContent());
        System.out.println("motomessage: " + messageDto.getContent());

        messageRepository.save(message);

    }


}
