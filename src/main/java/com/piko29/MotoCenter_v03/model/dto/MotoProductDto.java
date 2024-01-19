package com.piko29.MotoCenter_v03.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;


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
    //20.12 username of owner
    private String owner;

    //19.01 buying
    private Boolean sold;
    private String buyer;

}
