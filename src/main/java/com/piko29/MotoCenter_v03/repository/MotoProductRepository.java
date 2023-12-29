package com.piko29.MotoCenter_v03.repository;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotoProductRepository extends JpaRepository<MotoProduct, Long> {
    //29.12 long zmienione na Long i Optional
    Optional<MotoProduct> findById(Long id);
    MotoProduct findByTitle(String title);
    List<MotoProduct> findAllByOrderByIdAsc();
    //List<MotoProduct> findAllByCategoryId();//sorting and categories, currenty unused
}
