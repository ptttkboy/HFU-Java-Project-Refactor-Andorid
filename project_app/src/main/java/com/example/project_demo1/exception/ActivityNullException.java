package com.example.project_demo1.exception;

@Deprecated
public class ActivityNullException extends RuntimeException {
    public ActivityNullException() {
        super();
    }

    public ActivityNullException(String message) {
        super(message);
    }

    public ActivityNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityNullException(Throwable cause) {
        super(cause);
    }
}
