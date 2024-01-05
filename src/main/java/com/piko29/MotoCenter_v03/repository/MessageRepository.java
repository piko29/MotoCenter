package com.piko29.MotoCenter_v03.repository;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//later
public interface MessageRepository extends JpaRepository<Message, Long> {
    //04.01 for showing message
//    Optional<Message> findByEmail(String email);
}
