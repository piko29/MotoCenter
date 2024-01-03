package com.piko29.MotoCenter_v03.service;

import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.dto.*;
import com.piko29.MotoCenter_v03.repository.MotoProductRepository;
import com.piko29.MotoCenter_v03.repository.UserRepository;
import com.piko29.MotoCenter_v03.repository.UserRoleRepository;
import com.piko29.MotoCenter_v03.model.User;
import com.piko29.MotoCenter_v03.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    //added 06.12 to check motoproduct by person id
    private final MotoProductDtoMapper motoProductDtoMapper;
    //added 14.12
    private final MotoProductRepository motoProductRepository;


    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }


    public List<String> findAllUserEmails() {
        return userRepository.findAllUsersByRoles_Name(USER_ROLE)
                .stream()
                .map(User::getEmail)
                .toList();
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        if (isCurrentUserAdmin()) {
            userRepository.deleteByEmail(email);
        }
    }

    @Transactional
    public void register(UserRegistrationDto registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        String passwordHash = passwordEncoder.encode(registration.getPassword());
        user.setPassword(passwordHash);
        Optional<UserRole> userRole = userRoleRepository.findByName(USER_ROLE);
        userRole.ifPresentOrElse(
                role -> user.getRoles().add(role),
                () -> {
                    throw new NoSuchElementException();
                }
        );
        userRepository.save(user);
    }

    private boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isCurrentUserLogged() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> (authority.getAuthority().equals("ROLE_USER"))
                        || authority.getAuthority().equals("ROLE_ADMIN"));
    }


    @Transactional
    public void changeCurrentUserPassword(String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername).orElseThrow();
        String newPasswordHash = passwordEncoder.encode(newPassword);
        currentUser.setPassword(newPasswordHash);
    }


    //added 06.12
    public String getNameFromContextHolder() {

        return SecurityContextHolder.getContext()
                .getAuthentication().getName();

    }
    //later for message, check
    public User getUserById(Long id) {
        return new User();
    }

    //all user data, not necessary currently, might delete
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    //added 09.12
    public List<MotoProductDto> getProductsByUsername(String username) {
        System.out.println(userRepository.findByEmail(username)
                .map(User::getMotoProductList)
                .orElse(Collections.emptyList())
                .stream()
                .map(motoProductDtoMapper::map)
                .toList());
        return userRepository.findByEmail(username)
                .map(User::getMotoProductList)
                .orElse(Collections.emptyList())
                .stream()
                .map(motoProductDtoMapper::map)
                .toList();

    }
    //added 14.12

    //firstly show details
    public List<MotoProduct> getMotoProductById(Long id){
        return motoProductRepository.findById(id).stream().toList();
    }
    //delete product 14.12
    //make sure if it should be @Transactional
    @Transactional
    public void deleteMotoProduct(Long id){
        motoProductRepository.deleteById(id);
    }
    //29.12 for controller
    @Transactional
    public MotoProduct findMotoProduct(Long id){
        return motoProductRepository.findById(id).orElseThrow();
    }
    //29.12 for controller ending

    //add motoproduct 20.12
@Transactional
    public void saveMotoProduct(MotoProductDto dto) {
        MotoProduct motoProduct = new MotoProduct();
        motoProduct.setTitle(dto.getTitle());
        motoProduct.setDescription(dto.getDescription());
        //eventually implement adding pictures to database
        motoProduct.setImage("example.jpg");
        motoProduct.setPrice(dto.getPrice());
        motoProduct.setContactInfo(dto.getContactInfo());
        User user = userRepository.findByEmail(getNameFromContextHolder()).orElseThrow();//important line
        motoProduct.setUser(user);
//        motoProduct.getUser().setId(motoProduct.getUser().getId()); //not needed
        motoProduct.setOwner(getNameFromContextHolder());

        motoProductRepository.save(motoProduct);
    }

    //edit motoproduct 23.12
@Transactional
    public void editMotoProduct(MotoProductDto dto, Long id){
        MotoProduct motoProduct = motoProductRepository.findById(id).orElseThrow();
        motoProduct.setTitle(dto.getTitle());
        motoProduct.setDescription(dto.getDescription());
        motoProduct.setPrice(dto.getPrice());
        motoProduct.setContactInfo(dto.getContactInfo());

        motoProductRepository.save(motoProduct);
}



}
