package com.piko29.MotoCenter_v03.service;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDto;
import com.piko29.MotoCenter_v03.model.dto.MotoProductDtoMapper;
import com.piko29.MotoCenter_v03.repository.MotoProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MotoProductService {
    private final MotoProductRepository motoProductRepository;
    private final MotoProductDtoMapper motoProductDtoMapper;

    public List<MotoProduct> findAllMotoProducts() {
        return motoProductRepository.findAll().stream()
                .toList();
    }

    public List<MotoProduct> getMotoProductById(Long id) {
        return motoProductRepository.findById(id).stream().toList();
    }

}
