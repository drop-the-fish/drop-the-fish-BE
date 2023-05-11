package com.example.dropthefishbackendrdb.common.exception;

public enum ErrorCode {
    /*
    400 Bad Request
     */
    WRONG_IMAGE(40000),
    /*
    500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(50000),
    ;

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
