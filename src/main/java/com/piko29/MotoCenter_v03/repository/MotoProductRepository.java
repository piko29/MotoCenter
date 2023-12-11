package com.piko29.MotoCenter_v03.repository;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotoProductRepository extends JpaRepository<MotoProduct, Long> {
    MotoProduct findById(long id);
    MotoProduct findByTitle(String title);
    List<MotoProduct> findAllByOrderByIdAsc();
    //List<MotoProduct> findAllByCategoryId();//sorting and categories, currenty unused
}
