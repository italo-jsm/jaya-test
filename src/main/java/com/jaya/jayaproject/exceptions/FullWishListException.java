package com.jaya.jayaproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FullWishListException extends RuntimeException{
    public FullWishListException(String message) {
        super(message);
    }
}
