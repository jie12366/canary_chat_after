package com.coder.nettychat.entity.vo;

import lombok.Data;

/**
 * @author monkJay
 * @description 好友请求发送方的信息
 * @date 2020/1/10 21:13
 */
@Data
public class FriendRequestVO {
    /**
     * 发送者账号ID
     */
    private String sendUserId;
    /**
     * 发送者昵称
     */
    private String sendNickname;
    /**
     * 发送者头像
     */
    private String sendFaceImage;
    /**
     * 验证信息
     */
    private String verifyMessage;
    /**
     * 请求的状态
     */
    private String requestStatus;
}