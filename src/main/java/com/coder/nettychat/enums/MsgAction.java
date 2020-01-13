package com.coder.nettychat.enums;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 15:40
 */
public enum MsgAction {

    /**
     * 消息的动作
     */
    CONNECT(1, "初始化连接"),
    CHAT(2, "聊天信息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "客户端保持心跳");

    public final Integer type;
    public final String msg;

    MsgAction(Integer type, String msg){
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}