package com.wabc.utils;

public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    USERNAME_USED(501,"userNameUsed"),
    USERNAME_NOT_EXIST(502,"usernameNotExist"),
    PASSWORD_ERROR(503,"passwordError"),
    NOT_LOGIN(504,"notLogin"),
    CODE_ERROR(505,"codeError"),
    UNDEFINED_ERROR(510,"undefinedError")
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}
