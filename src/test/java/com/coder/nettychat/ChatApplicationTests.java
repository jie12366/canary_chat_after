package com.coder.nettychat;

import com.coder.nettychat.service.impl.ChatMsgServiceImpl;
import com.coder.nettychat.service.impl.UserServiceImpl;
import com.coder.nettychat.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ChatApplicationTests {

    @Resource
    UserServiceImpl userService;

    @Resource
    ChatMsgServiceImpl chatMsgService;

    @Test
    void testQuery(){
        LogUtil.info("查找账号:[{}]", userService.queryUsersByUsername("mon"));
    }

    @Test
    void testBatchSignedMsg(){
        List<String> list = new ArrayList<>();
        list.add("11");
        chatMsgService.batchSignMsg(list);
    }
}
