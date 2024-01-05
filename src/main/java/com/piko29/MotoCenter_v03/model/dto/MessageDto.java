package com.piko29.MotoCenter_v03.model.dto;

import lombok.Getter;
import lombok.Setter;

//04.01
@Getter
@Setter
public class MessageDto {
    //firstly without productId
    private Long messageId;
    private String title;
    private String content;
    private String sender;
    private String recipient;
}
