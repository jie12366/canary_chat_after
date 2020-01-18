package com.coder.nettychat.service.impl;

import com.coder.nettychat.component.FastdfsClient;
import com.coder.nettychat.entity.ChatMsg;
import com.coder.nettychat.enums.MsgSignStatus;
import com.coder.nettychat.enums.MsgType;
import com.coder.nettychat.mapper.ChatMsgMapper;
import com.coder.nettychat.mapper.CustomMsgMapper;
import com.coder.nettychat.entity.bo.ChatMsgBo;
import com.coder.nettychat.service.ChatMsgService;
import com.coder.nettychat.utils.file.Base64DecodeMultipartFile;
import com.coder.nettychat.utils.file.FileUtil;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
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
    FastdfsClient fastdfsClient;

    @Resource
    CustomMsgMapper customMsgMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveMsg(ChatMsgBo chatMsgBo) {
        String content = "";
        if ((MsgType.TEXT.type.equals(chatMsgBo.getType()))) {
            content = chatMsgBo.getContent();
        } else if (MsgType.IMGAE.type.equals(chatMsgBo.getType())
                || MsgType.AUDIO.type.equals(chatMsgBo.getType())) {
            MultipartFile multipartFile = Base64DecodeMultipartFile.base64ToMultipartFile(chatMsgBo.getContent());
            try {
                content = FileUtil.IMG_SERVER_URL + fastdfsClient.uploadFile(multipartFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ChatMsg chatMsg = new ChatMsg();
        // 将前端传来的信息设置到ChatMsg对象中
        chatMsg.setSendUserId(chatMsgBo.getSenderId());
        chatMsg.setAcceptUserId(chatMsgBo.getReceiverId());
        chatMsg.setType(chatMsgBo.getType());
        chatMsg.setMsg(content);
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

    @Override
    public List<ChatMsg> getUnsignedMsg(String acceptId) {
        Example example = new Example(ChatMsg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("acceptUserId", acceptId);
        criteria.andEqualTo("signFlag", 0);
        // 查找该接收者所有未签收的消息并返回
        return chatMsgMapper.selectByExample(example);
    }
}