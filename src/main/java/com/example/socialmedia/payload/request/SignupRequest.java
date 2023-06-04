package com.example.socialmedia.payload.request;

import com.example.socialmedia.annotations.PasswordMatches;
import com.example.socialmedia.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @Email(message = "It should be email format")
    @NotBlank(message = "User email is required" )
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Please enter your username (nickname)" )
    private String username;

    @NotEmpty(message = "Please enter your password")
    @Size(min = 5, max = 50)
    private String password;

    @NotEmpty(message = "Please enter your password again")
    private String confirmPassword;
}

