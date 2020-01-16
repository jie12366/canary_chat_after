package com.coder.nettychat.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author monkJay
 * @description 好友请求发送方的信息
 * @date 2020/1/10 21:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
     * 接收者账号
     */
    private String acceptUsername;
    /**
     * 接收者备注
     */
    private String acceptRemark;
    /**
     * 验证信息
     */
    private String verifyMessage;
    /**
     * 请求的状态
     */
    private String requestStatus;
}