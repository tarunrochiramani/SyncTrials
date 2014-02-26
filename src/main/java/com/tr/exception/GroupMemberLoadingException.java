package com.tr.exception;

public class GroupMemberLoadingException extends LoadingException {
    private static final String errorMsg = "Unable to load Groups from source";

    public GroupMemberLoadingException(Throwable cause) {
        super(errorMsg, cause);
    }
}
