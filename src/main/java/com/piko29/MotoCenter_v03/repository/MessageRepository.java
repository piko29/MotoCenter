package com.piko29.MotoCenter_v03.repository;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.dto.MessageDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//later
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findMessageBySender(User sender);
    List<Message> findMessagesBySender(User sender);
    List<Message> findMessagesByUser(User user);

}
