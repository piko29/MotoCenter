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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    //added 04.01
    private final MessageRepository messageRepository;//to check
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
    //reading message about motoproduct 04.01
    public List<MessageDto> getMessagesByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(User::getMessageList)
                .orElse(Collections.emptyList())
                .stream()
                .map(messageDtoMapper::map)
                .toList();

    }
    //deleting message 05.01
    @Transactional
    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }

    //answer message 05.01
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

        System.out.println("answer message working fine");
        messageRepository.save(message);

    }

    //10.01 chat feature
    //show messages from one user about one topic(visible) as 1 element
    //add option "details","delete" to this option
    //click on details and open messages to you and from you about 1 topic
    //below automatically show TopicTextSendMessage form(as in MotoProduct PM)
    //as a continuation of the conversation
    //add option to delete your single message
    //add option to delete whole conversation

    public List<MessageDto> getMessagesSentByOwner() {
        return messageRepository.findMessagesBySender
                        (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .toList();

    }

//15.01 modified
    public Set<String> getMessageSenders(){
        List<MessageDto> allReceivedMessages = getMessagesByUsername(getNameFromContextHolder());

        Set<String> senders = allReceivedMessages.stream()
                .map(MessageDto::getSender)
                .collect(Collectors.toSet());

        return senders;

    }
//15.01 chat with specific user, 17.01 updated with owner messages
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


}






