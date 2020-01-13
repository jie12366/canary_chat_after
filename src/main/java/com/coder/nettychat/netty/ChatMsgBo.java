package com.coder.nettychat.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 16:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsgBo {

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 发送者的账号id
     */
    private String senderId;

    /**
     * 接收者的账号id
     */
    private String receiverId;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 信息内容
     */
    private String content;
}