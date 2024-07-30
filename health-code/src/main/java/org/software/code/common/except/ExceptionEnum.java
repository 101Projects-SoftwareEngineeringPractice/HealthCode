package org.software.code.common.except;

public enum ExceptionEnum {
    /**
     * 错误码规范，便于通过错误码快速定位服务
     * 0、gateway   服务错误码统一以 0 开头
     * 1、user   服务错误码统一以 1 开头
     * 2、health-code    服务错误码统一以 2 开头
     * 3、itinerary-code  服务错误码统一以 3 开头
     * 4、nucleic-acids  服务错误码统一以 4 开头
     * 5、place-code  服务错误码统一以 5 开头
     */
    RUN_EXCEPTION("00001", "服务执行错误，请稍后重试"),
    TOKEN_EXPIRED("00002", "Token异常或已过期，请重新登录"),
    DATETIME_FORMAT_ERROR("00003", "日期时间格式错误"),
    BEAN_FORMAT_ERROR("00004", "数据参数异常"),
    FEIGN_EXCEPTION("00005", "服务通信异常，请稍后重试"),
    TOKEN_NOT_FIND("00006", "没有提供Token"),
    REQUEST_PARAMETER_ERROR("00007", "请求参数异常"),

    HEALTH_CODE_EXIST("30001", "健康码已存在"),
    HEALTH_CODE_NOT_FIND("30002", "健康码不存在，请申请"),
    HEALTH_CODE_INSERT_FAIL("30003", "健康码添加失败"),
    HEALTH_CODE_UPDATE_FAIL("30004", "健康码更新失败"),
    HEALTH_CODE_DELETE_FAIL("30005", "健康码删除失败"),
    HEALTH_CODE_SELECT_FAIL("30006", "健康码查询失败"),
    HEALTH_CODE_EVENT_INVALID("3007", "健康码事件无效"),

    ;
    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String msg;


    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
