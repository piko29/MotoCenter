package com.piko29.MotoCenter_v03.repository;

import com.piko29.MotoCenter_v03.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
//later
public interface MessageRepository extends JpaRepository<Message, Long> {
}
