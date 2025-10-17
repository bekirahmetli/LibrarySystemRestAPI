package com.example.core.utils;

import com.example.core.result.Result;
import com.example.core.result.ResultData;
import com.example.dto.response.CursorResponse;
import org.springframework.data.domain.Page;

public class ResultHelper {

    public static <T>ResultData<T> created(T data){
        return new ResultData<>(true, Message.CREATED,"201",data);
    }

    public static <T> ResultData<T> success(T data){
        return new ResultData<>(true,Message.SUCCESS,"200",data);
    }

    public static <T> ResultData<CursorResponse<T>> cursor (Page<T> pageData) {
        CursorResponse<T> cursor = new CursorResponse<>();
        cursor.setItems(pageData.getContent());
        cursor.setPageNumber(pageData.getNumber());
        cursor.setPageSize(pageData.getSize());
        cursor.setTotalElements(pageData.getTotalElements());
        return ResultHelper.success(cursor);
    }

    public static Result ok(){
        return new Result(true,Message.SUCCESS,"200");
    }
}
