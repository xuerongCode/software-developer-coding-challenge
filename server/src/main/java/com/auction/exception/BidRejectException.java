package com.auction.exception;

import org.springframework.http.HttpStatus;

public class BidRejectException extends RuntimeException {
    public BidRejectException(String message) {
        super(message);
    }
}
