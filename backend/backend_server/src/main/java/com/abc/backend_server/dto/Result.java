package com.abc.backend_server.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Result {
    private Boolean success;
    private String msg;
    private Object data;


    public Result(Boolean success, String msg, Object data, Long total) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }


    public static Result ok(){
        return new Result(true, null, null, null);
    }
    public static Result ok(Object data){
        return new Result(true, null, data, null);
    }
    public static Result ok(List<?> data, Long total){
        return new Result(true, null, data, total);
    }
    public static Result fail(String msg){
        return new Result(false, msg, null, null);
    }

}
