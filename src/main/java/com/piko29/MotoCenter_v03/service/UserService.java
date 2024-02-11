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
        //12.02 kolejna wiadomosc na ten sam temat ode mnie
        if(sourceMessage.getSender().getEmail().equals(getNameFromContextHolder())){
        message.setUser(sourceMessage.getUser());
            System.out.println("1:"+ sourceMessage.getUser());
            message.setSender(sender);
            message.setProductId(sourceMessage.getProductId());
            message.setTitle(sourceMessage.getTitle());
//            message.setUser(sourceMessage.getSender());//odbiorca
            message.setContent(dto.getContent());
        } else {
            //12.02
            message.setSender(sender);
            message.setProductId(sourceMessage.getProductId());
            message.setTitle(sourceMessage.getTitle());
            message.setUser(sourceMessage.getSender());//odbiorca
            System.out.println("2:????"+ sourceMessage.getSender());
            message.setContent(dto.getContent());

        }

        messageRepository.save(message);
    }

    public List<MessageDto> getMessagesSentByAccountOwner() {
        return messageRepository.findMessagesBySender
                        (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .toList();

    }

    public Set<String> getMessageSenders(){
        List<MessageDto> allReceivedMessages = getMessagesByUsername(getNameFromContextHolder());
        //ci ktorzy mi wyslali
        Set<String> senders = allReceivedMessages.stream()
                .map(MessageDto::getSender)
                .collect(Collectors.toSet());

        return senders;

    }
    //07.02 zmiana nazwy z getownersinglemessages 12.02
    public Set<String> getMessageRecipients(){
        List<MessageDto> allSentMessages = getMessagesSentByAccountOwner();
        //do ktorych ja wyslalem
        Set<String> recipients = allSentMessages.stream()
                .map(MessageDto::getRecipient)
                .collect(Collectors.toSet());

        return recipients;
    }
    //12.02 do obslugi interakcji przez wiadomosci z jednym uzytkownikiem
    public Set<String> getMessageRecipientsAndSenders(){
        List<MessageDto> allReceivedMessages = getMessagesByUsername(getNameFromContextHolder());
        //ci ktorzy mi wyslali
        Set<String> senders = allReceivedMessages.stream()
                .map(MessageDto::getSender)
                .collect(Collectors.toSet());

        List<MessageDto> allSentMessages = getMessagesSentByAccountOwner();
        //do ktorych ja wyslalem
        Set<String> recipients = allSentMessages.stream()
                .map(MessageDto::getRecipient)
                .collect(Collectors.toSet());

        Set<String> interactionWithUser = new HashSet<>(senders);
        interactionWithUser.addAll(recipients);

        return interactionWithUser;

    }
    //12.02

    //zmiana 11.02 do odczytywania rowniez wyslanych wiadomosci

    public List<MessageDto> chatWithUser(String email){
        //gdzie dostalem wiadomosc od {email} i gdzie ja jestem odbiorca
        List<MessageDto> received = messageRepository.findMessagesBySender
                        (userRepository.findByEmail(email).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .filter(messageDto -> messageDto.getRecipient().equals(getNameFromContextHolder()))
                .toList();
        //gdzie ja jestem wysylajacym i gdzie odbiorca to ten z {email}
        List<MessageDto> sent = messageRepository.findMessagesBySender
                        (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .filter(messageDto -> messageDto.getRecipient().equals(email)
                        || messageDto.getRecipient().equals(getNameFromContextHolder()))//tu byla zmiana
                .toList();

        return Stream.concat(received.stream(),sent.stream()).sorted(Comparator.comparing(MessageDto::getMessageId))
                .collect(Collectors.toList());

    }
    //07.02
    public List<MessageDto> chatSingleMessages(String email){

        //gdzie wysylajacym jestem ja i odbiorca inny niż z {email} ja
        List<MessageDto> sent = messageRepository.findMessagesBySender
                        (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow())
                .stream()
                .map(messageDtoMapper::map)
                .filter(messageDto -> !messageDto.getRecipient().equals(email))//odbiorca rozny niz ja {email}
                .sorted(Comparator.comparing(MessageDto::getMessageId))
                .toList();

//        List<MessageDto> received = messageRepository.findMessagesByUser
//                        (userRepository.findByEmail(email).orElseThrow())
//                .stream()
//                .map(messageDtoMapper::map)
//                .filter(messageDto -> messageDto.getRecipient().equals(getNameFromContextHolder()))
//                .toList();

        return sent;

    }
    //
    //09.02



    //06.02 working fine
    @Transactional
    public void deleteChatWithUser(String email){
        List<Message> received = messageRepository.findMessagesBySender
                (userRepository.findByEmail(email).orElseThrow()).stream().toList();

        List<Message> sent = messageRepository.findMessagesBySender
                (userRepository.findByEmail(getNameFromContextHolder()).orElseThrow()).stream()
                .filter(message -> message.getUser().getEmail().equals(email)).toList();

        List<Message> all=Stream.concat(received.stream(),sent.stream()).toList();

    messageRepository.deleteAll(all);
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






