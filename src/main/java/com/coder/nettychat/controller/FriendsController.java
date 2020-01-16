package com.coder.nettychat.controller;

import com.coder.nettychat.entity.Users;
import com.coder.nettychat.enums.ResultCode;
import com.coder.nettychat.enums.SearchFriendsStatus;
import com.coder.nettychat.service.FriendService;
import com.coder.nettychat.service.UserService;
import com.coder.nettychat.utils.result.JsonResult;
import com.coder.nettychat.utils.result.ReturnVOUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author monkJay
 * @description 好友相关请求的处理
 * @date 2020/1/10 14:27
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Resource
    UserService userService;
    @Resource
    FriendService friendService;

    @PostMapping("/")
    public JsonResult addFriend(String friendUsername){
        // 0. 判断好友账号不能为空
        if (StringUtils.isBlank(friendUsername)) {
            return JsonResult.failure(ResultCode.PARAM_IS_BLANK);
        }

        // 1. 查找好友的信息并返回
        Users users = userService.queryUsersByUsername(friendUsername);
        return JsonResult.success(ReturnVOUtil.copyToUsersVO(users));
    }

    /**
     * 搜索好友，做匹配查询而不是模糊查询
     * @param myUserId 我的账号id
     * @param friendUsername 好友账号
     * @return JsonResult
     */
    @PostMapping("/search")
    public JsonResult searchFriend(String myUserId, String friendUsername){
        // 0. 判断账号id和好友账号不能为空
        if (StringUtils.isBlank(myUserId)|| StringUtils.isBlank(friendUsername)) {
            return JsonResult.failure(ResultCode.PARAM_IS_BLANK);
        }

        // 获取搜索好友的结果
        Integer status = friendService.preconditionSearchFriends(myUserId,friendUsername);
        if(status.equals(SearchFriendsStatus.SUCCESS.status)){
            // 查找好友的信息并返回
            Users users = userService.queryUsersByUsername(friendUsername);
            return JsonResult.success(ReturnVOUtil.copyToUsersVO(users));
        }
        return JsonResult.failure(SearchFriendsStatus.getMsgByKey(status));
    }

    /**
     * 查询账号对应的账户信息
     * @param username 账号
     * @return 对应的账号信息
     */
    @PostMapping("/username")
    public JsonResult scanFriendQrcode(String username){
        // 查找好友的信息并返回
        Users users = userService.queryUsersByUsername(username);
        return JsonResult.success(ReturnVOUtil.copyToUsersVO(users));
    }

    /**
     * 返回我的好友列表
     * @param userId 我的账号id
     * @return 好友列表
     */
    @GetMapping("/list/{userId}")
    public JsonResult getFriendsList(@Valid @PathVariable("userId")String userId){
        return JsonResult.success(friendService.queryMyFriendsList(userId));
    }
}