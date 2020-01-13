package com.coder.nettychat.enums;

/**
 * @author monkJay
 * @description 统一返回状态码
 * @date 2020/1/7 22:15
 */
public enum ResultCode {

    /*成功状态码*/
    SUCCESS(200,"成功"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    USER_OR_PWD_NULL(20005, "账号或密码为空"),

    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "某业务出现问题"),
    SAVE_ERROR(30002,"保存发生错误"),
    UPDATE_ERROR(30003,"更新发生错误"),

    /* 数据错误：50001-599999 */
    RESULE_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),
    CAPTCHA_HAS_EXPIRED(50004,"验证码已过期"),
    CAPTCHA_IS_ERROR(50005,"验证码错误");

    /**状态码*/
    private Integer code;

    /**状态码描述*/
    private String message;

    ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer code(){
        return this.code;
    }

    public String message(){
        return this.message;
    }
}