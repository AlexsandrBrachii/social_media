package com.example.socialmedia.security;

public class SecurityConstants {
        public static final String SIGN_UP_URLS = "/api/auth/**";
        public static final String SECRET_KEY = "koshacek";
        public static final String TOKEN_PREFIX= "Bearer ";
        public static final String HEADER_STRING= "Authorization";
        public static final String CONTENT_TYPE= "application/json";
        public static final long EXPIRATION_TIME = 3600_000;
    }

