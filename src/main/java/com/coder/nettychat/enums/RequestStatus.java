package com.coder.nettychat.enums;

import lombok.Data;

/**
 * @author monkJay
 * @description 好友请求的状态
 * @date 2020/1/10 23:40
 */
public enum RequestStatus {
    /** 等待验证 */
    WAITING_VERIFY(0, "等待验证"),
    /** 等待验证 */
    ALREADY_IGNORE(1, "已忽略"),
    /** 等待验证 */
    ALREADY_PASS(2, "已通过"),
    /** 等待验证 */
    ALREADY_EXPIRED(3, "已过期");

    public final Integer status;
    public final String message;

    RequestStatus(Integer status, String message){
        this.status = status;
        this.message = message;
    }

    /**
     * 根据枚举的key获取对应的value
     * @param key 枚举的key
     * @return 对应的值
     */
    public static String getMsgByKey(Integer key){
        for (RequestStatus requestStatus : values()){
            if (requestStatus.getStatus().equals(key)){
                return requestStatus.getMessage();
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}