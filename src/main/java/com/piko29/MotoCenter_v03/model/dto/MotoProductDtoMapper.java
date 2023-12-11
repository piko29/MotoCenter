package com.piko29.MotoCenter_v03.model.dto;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MotoProductDtoMapper {

    private final UserRepository userRepository;
    public MotoProductDto map(MotoProduct motoProduct) {
        MotoProductDto dto = new MotoProductDto();
        dto.setId(motoProduct.getId());
        dto.setTitle(motoProduct.getTitle());
        dto.setDescription(motoProduct.getDescription());
        dto.setImage(motoProduct.getImage());
        dto.setPrice(motoProduct.getPrice());
        dto.setContactInfo(motoProduct.getContactInfo());
        //adding userid to motoproduct
        dto.setUserId(motoProduct.getUser().getId());
        return dto;
    }
    public MotoProduct map(MotoProductDto dto){
        MotoProduct motoProduct = new MotoProduct();
        motoProduct.setId(dto.getId());
        motoProduct.setTitle(dto.getTitle());
        motoProduct.setDescription(dto.getDescription());
        motoProduct.setImage(dto.getImage());
        motoProduct.setPrice(dto.getPrice());
        motoProduct.setContactInfo(dto.getContactInfo());
        //adding user id to motoproduct
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        motoProduct.setUser(user);
        return motoProduct;
    }
}
