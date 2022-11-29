package com.example.project_demo1.exception;

public class LoginException extends RuntimeException{

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
