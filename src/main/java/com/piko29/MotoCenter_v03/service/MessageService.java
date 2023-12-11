package com.piko29.MotoCenter_v03.service;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
//later
@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
    private UserService userService;

    public void sendMessage(Long senderId, Long recipientId, String content){
        User sender = userService.getUserById(senderId);
        User recipient = userService.getUserById(recipientId);

        if (sender != null && recipient != null) {
            Message message = new Message();
            message.setSender(sender);
            message.setRecipient(recipient);
            message.setContent(content);

            messageRepository.save(message);
        } else {
            //throw some exception
        }
    }
}
