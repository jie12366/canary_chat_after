package com.coder.nettychat.service;

import com.coder.nettychat.entity.ChatMsg;
import com.coder.nettychat.entity.bo.ChatMsgBo;

import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 16:25
 */
public interface ChatMsgService {

    /**
     * 保存消息内容
     * @param chatMsgBo 前端传来的消息
     * @return 返回消息的ID
     */
    String saveMsg(ChatMsgBo chatMsgBo);

    /**
     * 对消息进行批量签收
     * @param ids 消息ID集合
     */
    void batchSignMsg(List<String> ids);

    /**
     * 获取未签收的消息
     * @param acceptId 接收者id
     * @return List<ChatMsg>
     */
    List<ChatMsg> getUnsignedMsg(String acceptId);
}