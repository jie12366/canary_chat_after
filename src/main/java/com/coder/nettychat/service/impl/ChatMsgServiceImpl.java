package com.coder.nettychat.service.impl;

import com.coder.nettychat.entity.ChatMsg;
import com.coder.nettychat.enums.MsgSignStatus;
import com.coder.nettychat.mapper.ChatMsgMapper;
import com.coder.nettychat.mapper.CustomMsgMapper;
import com.coder.nettychat.netty.ChatMsgBo;
import com.coder.nettychat.service.ChatMsgService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 16:25
 */
@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Resource
    ChatMsgMapper chatMsgMapper;

    @Resource
    CustomMsgMapper customMsgMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveMsg(ChatMsgBo chatMsgBo) {
        ChatMsg chatMsg = new ChatMsg();
        // 将前端传来的信息设置到ChatMsg对象中
        chatMsg.setSendUserId(chatMsgBo.getSenderId());
        chatMsg.setAcceptUserId(chatMsgBo.getReceiverId());
        chatMsg.setType(chatMsgBo.getType());
        chatMsg.setMsg(chatMsgBo.getContent());
        // 设置唯一ID
        String msgId = Sid.nextShort();
        chatMsg.setId(msgId);
        // 设置消息状态为未签收
        chatMsg.setSignFlag(MsgSignStatus.UNSIGN.status);
        // 设置消息创建时间
        chatMsg.setCreateTime(new Date());
        chatMsgMapper.insert(chatMsg);
        return msgId;
    }

    @Override
    public void batchSignMsg(List<String> ids) {
        customMsgMapper.batchSignMsg(ids);
    }
}