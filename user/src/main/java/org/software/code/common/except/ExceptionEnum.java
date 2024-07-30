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


    UID_EXIST("20001", "该用户已存在"),
    ID_EXIST("20002", "该身份证已注册"),
    PHONE_EXIST("20003", "该手机号已注册"),
    UID_NOT_FIND("20004", "该用户不存在"),
    ID_NOT_FIND("20005", "该身份证用户不存在"),
    USER_INSERT_FAIL("20006", "用户添加失败"),
    USER_UPDATE_FAIL("20007", "用户更新失败"),
    USER_DELETE_FAIL("20008", "用户删除失败"),
    USER_SELECT_FAIL("20009", "用户查询失败"),
    PERMISSION_DENIED("20010", "该用户无权限"),
    USER_PASSWORD_ERROR("20011", "账户密码错误，请重试"),
    USER_BIND_INSERT_FAIL("20012", "绑定用户添加失败"),
    USER_BIND_UPDATE_FAIL("20013", "绑定用户更新失败"),
    USER_BIND_DELETE_FAIL("20014", "绑定用户删除失败"),
    USER_BIND_SELECT_FAIL("20015", "绑定用户查询失败"),

    NUCLEIC_ACID_TEST_USER_INSERT_FAIL("20016", "核酸用户添加失败"),
    NUCLEIC_ACID_TEST_USER_UPDATE_FAIL("20017", "核酸用户更新失败"),
    NUCLEIC_ACID_TEST_USER_DELETE_FAIL("20018", "核酸用户删除失败"),
    NUCLEIC_ACID_TEST_USER_SELECT_FAIL("20019", "核酸用户查询失败"),

    MANAGER_USER_INSERT_FAIL("20020", "管理用户添加失败"),
    MANAGER_USER_UPDATE_FAIL("20021", "管理用户更新失败"),
    MANAGER_USER_DELETE_FAIL("20022", "管理用户删除失败"),
    MANAGER_USER_SELECT_FAIL("20023", "管理用户查询失败"),

    USER_LOGIN_EXIST_NULL_FAIL("200024", "用户登录失败，请稍后重试"),

    MANAGER_USER_NOT_FIND("20025", "管理用户不存在"),
    NUCLEIC_ACID_TEST_USER_NOT_FIND("20016", "核酸用户不存在"),

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
