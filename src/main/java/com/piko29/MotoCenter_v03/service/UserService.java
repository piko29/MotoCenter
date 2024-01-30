package com.piko29.MotoCenter_v03.service;

import com.piko29.MotoCenter_v03.model.Message;
import com.piko29.MotoCenter_v03.model.MotoProduct;
import com.piko29.MotoCenter_v03.model.dto.*;
import com.piko29.MotoCenter_v03.repository.MessageRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.piko29.MotoCenter_v03.controller.UserController.UPLOAD_DIRECTORY;

@Service
@AllArgsConstructor
public class UserService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MotoProductDtoMapper motoProductDtoMapper;
    private final MotoProductRepository motoProductRepository;
    private final MessageRepository messageRepository;
    private final MessageDtoMapper messageDtoMapper;



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

    public String getNameFromContextHolder() {

        return SecurityContextHolder.getContext()
                .getAuthentication().getName();

    }

    public User getUserById(Long id) {
        return new User();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<MotoProductDto> getProductsByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(User::getMotoProductList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(motoProduct -> motoProduct.getSold() == false)
                .map(motoProductDtoMapper::map)
                .toList();

    }

    public List<MotoProduct> getMotoProductById(Long id){
        return motoProductRepository.findById(id).stream().toList();
    }

    @Transactional
    public void deleteMotoProduct(Long id){
        motoProductRepository.deleteById(id);
    }

    @Transactional
    public MotoProduct findMotoProduct(Long id){
        return motoProductRepository.findById(id).orElseThrow();
    }

@Transactional
    public void saveMotoProduct(MotoProductDto dto, String fileName) throws IOException {
        MotoProduct motoProduct = new MotoProduct();

        motoProduct.setTitle(dto.getTitle());
        motoProduct.setDescription(dto.getDescription());
        motoProduct.setImage(fileName);
        motoProduct.setPrice(dto.getPrice());
        motoProduct.setContactInfo(dto.getContactInfo());
        User user = userRepository.findByEmail(getNameFromContextHolder()).orElseThrow();
        motoProduct.setUser(user);
        motoProduct.setOwner(getNameFromContextHolder());
        motoProduct.setSold(false);
        motoProduct.setBuyer("");

        motoProductRepository.save(motoProduct);
    }

    @Transactional
    public void editMotoProduct(MotoProductDto dto, Long id){
        MotoProduct motoProduct = motoProductRepository.findById(id).orElseThrow();
        motoProduct.setTitle(dto.getTitle());
        motoProduct.setDescription(dto.getDescription());
        motoProduct.setPrice(dto.getPrice());
        motoProduct.setContactInfo(dto.getContactInfo());

        motoProductRepository.save(motoProduct);
    }

    public List<MessageDto> getMessagesByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(User::getMessageList)
                .orElse(Collections.emptyList())
                .stream()
                .map(messageDtoMapper::map)
                .toList();

    }

    @Transactional
    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }

    public Message findMessage(Long id){
        return messageRepository.findById(id).orElseThrow();}
    @Transactional
    public void answerMotoProductMessage(Message dto, Long id){
        Message sourceMessage = messageRepository.findById(id).orElseThrow();
        Message message = new Message();
        User sender = userRepository.findByEmail(getNameFromContextHolder()).orElseThrow();
        message.setSender(sender);
        message.setProductId(sourceMessage.getProductId());
        message.setTitle(sourceMessage.getTitle());
        message.setUser(sourceMessage.getSender());
        message.setContent(dto.getContent());

        messageRepository.save(message);
    }

    public List<MessageDto> getMessagesSentByOwner() {
        return messageRepository.findMessagesBySender
                        (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .toList();

    }

    public Set<String> getMessageSenders(){
        List<MessageDto> allReceivedMessages = getMessagesByUsername(getNameFromContextHolder());

        Set<String> senders = allReceivedMessages.stream()
                .map(MessageDto::getSender)
                .collect(Collectors.toSet());

        return senders;

    }
    public List<MessageDto> chatWithUser(String email){

        List<MessageDto> received = messageRepository.findMessagesBySender
                        (userRepository.findByEmail(email).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .toList();
        List<MessageDto> sent = messageRepository.findMessagesBySender
                        (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .filter(messageDto -> messageDto.getRecipient().equals(email))
                .toList();

        return Stream.concat(received.stream(),sent.stream()).sorted(Comparator.comparing(MessageDto::getMessageId))
                .collect(Collectors.toList());

    }

    public List<MotoProductDto> getSoldProductsByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(User::getMotoProductList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(motoProduct -> motoProduct.getSold() == true)
                .map(motoProductDtoMapper::map)
                .toList();

    }
    public List<MotoProduct> getBoughtProductsByUsername() {
        return motoProductRepository.findAll().stream()
                .filter(motoProduct -> motoProduct.getBuyer().equals(getNameFromContextHolder()))
                .toList();


    }


}






