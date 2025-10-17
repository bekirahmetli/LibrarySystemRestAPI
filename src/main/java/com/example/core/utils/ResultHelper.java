package com.example.core.utils;

import com.example.core.result.ResultData;

public class ResultHelper {

    public static <T>ResultData<T> created(T data){
        return new ResultData<>(true, Message.CREATED,"201",data);
    }

    public static <T> ResultData<T> success(T data){
        return new ResultData<>(true,Message.SUCCESS,"200",data);
    }
}
