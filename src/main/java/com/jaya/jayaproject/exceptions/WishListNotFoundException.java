package com.jaya.jayaproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WishListNotFoundException extends RuntimeException{
    public WishListNotFoundException() {
        super("WishList Not Found");
    }
}
