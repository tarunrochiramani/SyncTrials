package com.tr.exception;

public class GroupLoadingException extends Exception {

    private static final String errorMsg = "Unable to load Groups from source";

    public GroupLoadingException(Throwable cause) {
        super(cause);
    }

    public String toString() {
        return errorMsg + " - " + getCause();
    }
}
