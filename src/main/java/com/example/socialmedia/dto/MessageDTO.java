package com.example.socialmedia.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MessageDTO {

    private Long id;

    private String title;

    private String caption;


    @NotEmpty
    private String username;

    @NotEmpty
    private String message;

}

