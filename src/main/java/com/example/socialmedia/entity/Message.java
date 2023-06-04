package com.example.socialmedia.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private String username;


    private String title;

    private String caption;


    @Column(columnDefinition = "text", nullable = false)
    private String message;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd нн:mm:ss")
    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    public Message() {
    }
}


