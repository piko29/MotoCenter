package com.piko29.MotoCenter_v03.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private Long messageId;
    private String title;
    private String content;
    private String sender;
    private String recipient;
    private Long productId;
}
