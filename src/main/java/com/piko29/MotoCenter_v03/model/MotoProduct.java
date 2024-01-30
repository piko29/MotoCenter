package com.piko29.MotoCenter_v03.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;


@Entity
@Getter
@Setter
@Table(name = "moto_product")
public class MotoProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String image;
    private int price;
    private String contactInfo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String owner;
    private Boolean sold;
    private String buyer;
}
