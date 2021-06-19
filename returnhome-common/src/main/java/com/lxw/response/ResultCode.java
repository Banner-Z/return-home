package com.lxw.response;

/**
 * 定义返回码
 */
public enum ResultCode implements CustomizeResultCode{

    SUCCESS(200,"成功"),
    ERROR(500,"发生了未知错误"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 页面 */
    PAGE_REFUSE(403,"服务器拒绝"),
    PAGE_NOT_FOUND(404,"资源找不到"),

    /* 参数错误*/
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),


    ARITHMETIC_EXCEPTION(3006,"算术异常"),

    LOSTMAN_NOT_EXIST(3007,"该老人不存在"),
    ERROR_FILE_FORMAT(3008,"文件格式错误"),
    LOSTMAN_PIC_NOT_EXIST(3009,"该老人图片不存在"),
    LOSTMAN_AGE_NOT_RIGHT(3010,"该老人年龄不符合报案要求");

    ;

    private Integer code;
    private String message;

    ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
