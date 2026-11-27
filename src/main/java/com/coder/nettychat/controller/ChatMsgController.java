package com.coder.nettychat.controller;

import com.coder.nettychat.entity.ChatMsg;
import com.coder.nettychat.enums.ResultCode;
import com.coder.nettychat.service.ChatMsgService;
import com.coder.nettychat.utils.result.JsonResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/17 11:53
 */
@RestController
@RequestMapping("/msg")
public class ChatMsgController {

    @Resource
    ChatMsgService chatMsgService;

    @GetMapping("/unsignedMsg/{acceptId}")
    public JsonResult getNotSignedMsg(@PathVariable("acceptId") String acceptId){
        if (!StringUtils.hasText(acceptId)){
            return JsonResult.failure(ResultCode.PARAM_IS_BLANK);
        }
        List<ChatMsg> unsignedMsgList = chatMsgService.getUnsignedMsg(acceptId);
        return JsonResult.success(unsignedMsgList);
    }
}
