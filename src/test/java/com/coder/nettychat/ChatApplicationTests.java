package com.coder.nettychat;

import com.coder.nettychat.service.impl.ChatMsgServiceImpl;
import com.coder.nettychat.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ChatApplicationTests {

    @Resource
    ChatMsgServiceImpl chatMsgService;

    @Test
    void testBatchSignedMsg(){
        LogUtil.info("未签收的消息:[{}]", chatMsgService.getUnsignedMsg("200110042ZDF7P4H"));
    }
}
