package com.coder.nettychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author monkJay
 * @date 2020/1/7 19:44
 * @description 聊天信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg {
    /**
     * 信息id
     */
    @Id
    private String id;

    /**
     * 发送者的账号id
     */
    private String sendUserId;

    /**
     * 接收者的账号id
     */
    private String acceptUserId;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 信息内容
     */
    private String msg;

    /**
     * 是否已读标志
     */
    private Integer signFlag;

    /**
     * 发送时间
     */
    private Date createTime;
}