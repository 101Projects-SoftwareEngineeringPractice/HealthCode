package org.software.code.common.except;

public enum ExceptionEnum {
    /**
     * 错误码规范，便于通过错误码快速定位服务
     * 0、通用服务错误码统一以 0 开头
     * 1、gateway   服务错误码统一以 1 开头
     * 2、user   服务错误码统一以 2 开头
     * 3、health-code    服务错误码统一以 3 开头
     * 4、itinerary-code  服务错误码统一以 4 开头
     * 5、nucleic-acids  服务错误码统一以 5 开头
     * 6、place-code  服务错误码统一以 6 开头
     */
    RUN_EXCEPTION("00001", "服务执行错误，请稍后重试"),
    TOKEN_EXPIRED("00002", "Token异常或已过期，请重新登录"),
    DATETIME_FORMAT_ERROR("00003", "日期时间格式错误"),
    BEAN_FORMAT_ERROR("00004", "数据参数异常"),
    FEIGN_EXCEPTION("00005", "服务通信异常，请稍后重试"),
    TOKEN_NOT_FIND("00006", "没有提供Token"),
    REQUEST_PARAMETER_ERROR("00007", "请求参数异常"),

    NUCLEIC_ACID_TEST_INSERT_FAIL("50001", "核酸记录添加失败"),
    NUCLEIC_ACID_TEST_UPDATE_FAIL("50002", "核酸记录更新失败"),
    NUCLEIC_ACID_TEST_DELETE_FAIL("50003", "核酸记录删除失败"),
    NUCLEIC_ACID_TEST_SELECT_FAIL("50004", "核酸记录查询失败"),
    RISK_CALCULATION_NOT_FIND("50005", "地区风险策略不存在"),
    RETEST_NOTIFICATION_EXCEPTION("50006", "通知复检时异常"),
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
