package com.example.socialmedia.facade;

import com.example.socialmedia.dto.MessageDTO;
import com.example.socialmedia.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageFacade {
    public MessageDTO messageToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setUsername(message.getUser().getUsername());
        messageDTO.setCaption(message.getCaption());
        messageDTO.setTitle(message.getTitle());

        return messageDTO;
    }
}

