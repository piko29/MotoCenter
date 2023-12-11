package com.piko29.MotoCenter_v03.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "moto_product")
public class MotoProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String image;
    private int price;
    private String contactInfo;
    //added 06.12 to join userid
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    //you can add type and category maybe
}
