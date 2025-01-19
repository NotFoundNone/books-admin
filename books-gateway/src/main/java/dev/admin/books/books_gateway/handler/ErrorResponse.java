package dev.admin.books.books_gateway.handler;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String details;
    private String path;

    public ErrorResponse(int status, String error, String message, String details, String path) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getPath() {
        return path;
    }
}
