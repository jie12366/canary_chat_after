package com.coder.nettychat;

import com.coder.nettychat.service.UserService;
import com.coder.nettychat.service.impl.ChatMsgServiceImpl;
import com.coder.nettychat.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ChatApplicationTests {

    @Resource
    UserService userService;

    @Test
    void testBatchSignedMsg(){
        LogUtil.info("好友的信息:[{}]", userService.queryUsersByUsername("mon"));
    }
}
