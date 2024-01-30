package com.piko29.MotoCenter_v03.model.dto;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageDtoMapper {

    private final UserRepository userRepository;
    public MessageDto map(Message message) {
        MessageDto dto = new MessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setTitle(message.getTitle());
        dto.setContent(message.getContent());
        dto.setSender(message.getSender().getEmail());
        dto.setRecipient(message.getUser().getEmail());
        dto.setProductId(message.getProductId());
        return dto;
    }
    public Message map(MessageDto dto){
        Message message = new Message();
        message.setMessageId(dto.getMessageId());
        message.setTitle(dto.getTitle());
        message.setContent(dto.getContent());

        User sender = userRepository.findByEmail(dto.getSender()).orElseThrow();
        message.setSender(sender);
        User recipient = userRepository.findByEmail(dto.getRecipient()).orElseThrow();
        message.setUser(recipient);
        message.setProductId(dto.getProductId());

        return message;
    }
}
