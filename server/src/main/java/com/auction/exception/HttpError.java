package com.auction.exception;

import org.springframework.http.HttpStatus;

public class HttpError {
    private HttpStatus status;
    private String message;

    public HttpError() {
    }

    public HttpError(HttpStatus status) {
        this.status = status;
    }

    public HttpError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpError(HttpStatus status, Throwable t) {
        this.status = status;
        this.message = t.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
