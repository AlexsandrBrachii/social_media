package com.example.socialmedia.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    private Long id;

    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String info;

    public void setUsername(String username) {
    }
}


