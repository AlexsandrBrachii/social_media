package com.example.socialmedia.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VideoNotFoundException extends RuntimeException {
    public VideoNotFoundException(String message) {

        super(message);
    }
}

