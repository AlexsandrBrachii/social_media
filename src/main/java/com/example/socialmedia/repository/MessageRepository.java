package com.example.socialmedia.repository;

import com.example.socialmedia.entity.Message;
import com.example.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByOrderByCreateDate();

    List<Message> findAllByUserOrderByCreateDateDesc(User user);

    Optional<Message> findAllMessagesByUserId(Long userId);

    Optional<Message> findAllMessagesById(Long messageId);

    Optional<Message> findMessageByIdAndUser(Long id, User user);
}

