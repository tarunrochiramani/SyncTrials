package com.tr.exception;

public class GroupLoadingException extends LoadingException {

    private static final String errorMsg = "Unable to load Groups from source";

    public GroupLoadingException(Throwable cause) {
        super(errorMsg, cause);
    }
}
