package com.ok_jha.splitapp.exception;

/**
 * Base runtime exception for custom exceptions within the SplitApp application.
 * Helps distinguish application-specific errors from generic/system errors.
 */
public abstract class SplitAppException extends RuntimeException {
    public SplitAppException(String mesaage){
        super(mesaage);
    }

    public SplitAppException(String message, Throwable cause){
        super(message,cause);
    }
}
