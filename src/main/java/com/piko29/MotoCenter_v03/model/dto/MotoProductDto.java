package com.piko29.MotoCenter_v03.model.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MotoProductDto {
    private Long id;
    private String title;
    private String description;
    private String image;
    private int price;
    private String contactInfo;
    private Long userId;//zmienione na user 06.12 sprawdz
}
