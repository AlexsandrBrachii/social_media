package com.example.socialmedia.service;
import com.example.socialmedia.dto.MessageDTO;
import com.example.socialmedia.entity.*;
import com.example.socialmedia.exceptions.MessageNotFoundException;
import com.example.socialmedia.repository.MessageRepository;
import com.example.socialmedia.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
public class MessageService {
    public static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final  MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;

    }

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByCreateDate();
    }

    public Message getMessagesById(Long messageId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return messageRepository.findMessageByIdAndUser(messageId, user)
                .orElseThrow(() -> new MessageNotFoundException("Message not found for username:" + user.getEmail()));
    }

    public List<Message> getAllMessagesForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return messageRepository.findAllByUserOrderByCreateDateDesc(user);
    }


    public Message createMessage(MessageDTO messageDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Message message = new Message();
        message.setUser(user);
        message.setTitle(messageDTO.getTitle());
        message.setCaption(messageDTO.getCaption());

        LOG.info("Create new message for user: {}", user.getEmail());
        return messageRepository.save(message);
    }

    public void deleteMessage(Long messageId, Principal principal) {
            Optional<Message> message = messageRepository.findById(messageId);
            message.ifPresent(messageRepository::delete);
        }


    private User getUserByPrincipal(Principal principal) {
            String username = principal.getName();
            return userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        }
    }





