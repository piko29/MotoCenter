package com.piko29.MotoCenter_v03.repository;

import com.piko29.MotoCenter_v03.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllUsersByRoles_Name(String role);
    void deleteByEmail(String email);

}
