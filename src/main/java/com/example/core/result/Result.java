package com.example.core.result;

import lombok.Getter;

@Getter
public class Result {
    private Boolean status;
    private String message;
    private String code;

    public Result(Boolean status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
