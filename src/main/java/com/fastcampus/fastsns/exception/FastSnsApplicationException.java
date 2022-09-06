package com.fastcampus.fastsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FastSnsApplicationException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public FastSnsApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        }
        return String.format("%s, $s", errorCode.getMessage(), message);
    }
}
