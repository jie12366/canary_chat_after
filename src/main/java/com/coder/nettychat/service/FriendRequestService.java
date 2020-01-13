package com.coder.nettychat.service;

import com.coder.nettychat.entity.vo.FriendRequestVO;

import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/11 0:10
 */
public interface FriendRequestService {

    /**
     * 发送好友请求
     * @param myUserId 我的账号id
     * @param friendUsername 好友账号
     */
    void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * 根据账号id查找发送添加请求的好友列表
     * @param userId 我的账号id
     * @return 发送添加请求的好友列表
     */
    List<FriendRequestVO> queryRequestByUserId(String userId);

    /**
     * 更新请求的状态
     * @param userId 我的账号id
     * @param friendId 好友的账号id
     * @param type 验证的状态
     */
    void updateRequestStatus(String userId, String friendId, Integer type);

    /**
     * 查询请求是否被处理
     * @param userId 我的账号id
     * @param friendId 好友的账号id
     * @return 是否被处理
     */
    boolean queryRequestIsHandled(String userId, String friendId);
}