package com.piko29.MotoCenter_v03.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
//later
@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private Long productId;
    private String title;
    private String content;
    @ManyToOne //make sure if onetoone it is the best relation
//    @JoinColumn(name = "user_id")
    private User sender;
    @ManyToOne //the same
    private User recipient;

}
