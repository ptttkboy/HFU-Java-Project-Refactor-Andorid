package com.example.project_demo1.exception;

import java.io.IOException;

public class ImageDownloadException extends RuntimeException {
    public ImageDownloadException(IOException e) {
        super();
    }

    public ImageDownloadException(String message) {
        super(message);
    }

    public ImageDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
