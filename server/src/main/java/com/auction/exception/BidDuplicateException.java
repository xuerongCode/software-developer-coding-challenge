package com.auction.exception;

public class BidDuplicateException extends RuntimeException {
    public BidDuplicateException(String message) {
        super(message);
    }
}
