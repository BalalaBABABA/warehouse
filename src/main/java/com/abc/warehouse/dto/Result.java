package com.abc.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Result {
    private Boolean success;
    private int code;
    private String errorMsg;
    private Object data;
    private Long total;
    private String aesKey;



    public Result(Boolean success, String errorMsg, Object data, Long total) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.data = data;
        this.total = total;
    }

    public Result(Boolean success, Object data, String aesKey) {
        this.success = success;
        this.data = data;
        this.aesKey = aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
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

//    public static Result ok(Object data,String aesKey,String publicKey){
//        Result result = new Result();
//        result.setSuccess(true);
//        result.setData(data);
//        result.setAesKey(aesKey);
//        return result;
//    }
    public static Result fail(String errorMsg){
        return new Result(false, errorMsg, null, null);
    }

}
