package com.coder.nettychat.controller;

import com.coder.nettychat.entity.vo.FriendRequestVO;
import com.coder.nettychat.enums.ResultCode;
import com.coder.nettychat.enums.SearchFriendsStatus;
import com.coder.nettychat.service.FriendRequestService;
import com.coder.nettychat.service.FriendService;
import com.coder.nettychat.utils.LogUtil;
import com.coder.nettychat.utils.result.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author monkJay
 * @description
 * @date 2020/1/11 0:13
 */
@RestController
public class FriendRequestController {

    @Autowired
    FriendRequestService friendRequestService;

    @Autowired
    FriendService friendService;

    @PostMapping("/friendRequest")
    public JsonResult addFriendRequest(@RequestBody FriendRequestVO friendRequestVO){
        LogUtil.info("接收者备注：[{}]", friendRequestVO.getAcceptRemark());
        // 0. 判断账号id和好友账号不能为空
        if (StringUtils.isBlank(friendRequestVO.getSendUserId()) || StringUtils.isBlank(friendRequestVO.getAcceptUsername())
         || StringUtils.isBlank(friendRequestVO.getVerifyMessage()) || StringUtils.isBlank(friendRequestVO.getAcceptRemark())) {
            return JsonResult.failure(ResultCode.PARAM_IS_BLANK);
        }
        // 获取搜索好友的结果
        Integer status = friendService.preconditionSearchFriends(friendRequestVO.getSendUserId(),friendRequestVO.getAcceptUsername());
        if(status.equals(SearchFriendsStatus.SUCCESS.status)){
            // 发送添加好友的请求
            friendRequestService.sendFriendRequest(friendRequestVO);
            return JsonResult.success();
        }
        return JsonResult.failure(SearchFriendsStatus.getMsgByKey(status));
    }

    @GetMapping("/friendRequest/{userId}")
    public JsonResult getFriendRequest(@PathVariable("userId") @Valid String userId){
        // 返回好友请求列表
        return JsonResult.success(friendRequestService.queryRequestByUserId(userId));
    }

    @PutMapping("/friendRequest")
    public JsonResult operatorFriendRequest(@Valid String userId, @Valid String friendId,
                                            @Valid Integer type){
        boolean isHandle = friendRequestService.queryRequestIsHandled(userId, friendId);
        if (isHandle){
            return JsonResult.failure("请求已被处理过");
        }
        // 如果忽略了请求
        if (type == 1){
            // 直接更新请求的状态
            friendRequestService.updateRequestStatus(userId, friendId, type);
        }
        // 如果通过了请求
        else if(type == 2){
            // 通过请求，更新状态，互加好友
            friendService.passFriendRequest(userId, friendId, type);
            // 返回我的好友列表
            return JsonResult.success(friendService.queryMyFriendsList(userId));
        }
        return JsonResult.success();
    }
}