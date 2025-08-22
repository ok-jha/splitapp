package com.ok_jha.splitapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends SplitAppException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
