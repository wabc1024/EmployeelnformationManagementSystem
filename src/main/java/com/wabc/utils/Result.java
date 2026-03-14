package com.wabc.utils;

public class Result {
    public Integer code;
    public String message ;
    public Object data;

    public Result(){};

    public Result(Integer code, String message, Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public static Result ok(Object data){
        return new Result(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    public static Result build(ResultCodeEnum resultCodeEnum,Object data){
        return new Result(resultCodeEnum.getCode(),resultCodeEnum.getMessage(),data);
    }

}
