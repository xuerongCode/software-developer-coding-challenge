package com.auction.exception.handler;

import com.auction.exception.BidRejectException;
import com.auction.exception.HttpError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@EnableWebMvc
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(new HttpError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new HttpError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new HttpError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(value = BidRejectException.class)
    public ResponseEntity<Object> handBidRejectException(BidRejectException ex) {
        return buildResponseEntity(new HttpError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handRuntimeException(RuntimeException ex) {
        return buildResponseEntity(new HttpError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(HttpError httpError) {
        return new ResponseEntity<>(httpError, httpError.getStatus());
    }
}
