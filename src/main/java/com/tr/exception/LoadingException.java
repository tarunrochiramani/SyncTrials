package com.tr.exception;

public class LoadingException extends Exception {

    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }


    public String toString() {
        return getMessage() + " - " + getCause();
    }
}
