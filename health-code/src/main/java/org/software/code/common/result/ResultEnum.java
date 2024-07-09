package org.software.code.common.result;

public enum ResultEnum implements IResult {


    SUCCESS(200, "成功"),

    FAILED(400, "失败");

    private Integer code;

    private String message;

    ResultEnum() {
    }

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}