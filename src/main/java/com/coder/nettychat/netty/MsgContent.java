package com.coder.nettychat.netty;

import com.coder.nettychat.entity.bo.ChatMsgBo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 15:32
 */
@Data
@NoArgsConstructor
public class MsgContent {

    /**
     * 消息动作类型
     */
    private Integer action;

    /**
     * 聊天信息
     */
    private ChatMsgBo chatMsgBo;

    /**
     * 扩展字段
     */
    private String extend;
}