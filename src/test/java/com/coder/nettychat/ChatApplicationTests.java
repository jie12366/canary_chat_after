package com.coder.nettychat;

import com.coder.nettychat.entity.vo.FriendRequestVO;
import com.coder.nettychat.service.impl.ChatMsgServiceImpl;
import com.coder.nettychat.service.impl.FriendRequestServiceImpl;
import com.coder.nettychat.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ChatApplicationTests {

    @Resource
    FriendRequestServiceImpl requestService;

    @Resource
    ChatMsgServiceImpl chatMsgService;

    @Test
    void testBatchSignedMsg(){
        List<String> list = new ArrayList<>();
        list.add("11");
        chatMsgService.batchSignMsg(list);
    }

    @Test
    void getRequest(){
        List<FriendRequestVO> voList = requestService.queryRequestByUserId("200111F2BK2YASA8");
        LogUtil.info("请求列表为: [{}]", voList);
    }
}
