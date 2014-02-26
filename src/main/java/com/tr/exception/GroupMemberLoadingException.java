package com.tr.exception;

public class GroupMemberLoadingException extends Exception {
    private static final String errorMsg = "Unable to load Groups from source";

    public GroupMemberLoadingException(Throwable cause) {
        super(cause);
    }

    public String toString() {
        return errorMsg + " - " + getCause();
    }
}
