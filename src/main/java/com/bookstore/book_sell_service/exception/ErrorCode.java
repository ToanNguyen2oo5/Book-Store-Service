package com.bookstore.book_sell_service.exception;

public enum ErrorCode {
    UNCATEGORIZED_ERROR(999,"uncategorized errror"),
    USER_EXISTED(101,"user existed"),
    INVALID_USERNAME(102,"username must not be blank or has special characters"),
    INVALID_PASSWORD(103,"password must be at least 8 characters"),
    INVALID_KEY(104,"invalid message key"),
    USER_NOT_EXISTED(105,"user not existed"),
    UNAUTHENTICATED(106,"unauthenticated"),
    BLANK_PASSWORD(107,"password must not be blank"),
    INVALID_EMAIL(108,"invalid email")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
