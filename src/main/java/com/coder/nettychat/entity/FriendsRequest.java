package com.coder.nettychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author monkJay
 * @date 2020/1/7 19:44
 * @description 好友请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsRequest {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 发送者的账号id
     */
    private String sendUserId;

    /**
     * 接收者的账号id
     */
    private String acceptUserId;

    /**
     * 请求的时间
     */
    private Date requestDataTime;

    /**
     * 请求者发送的信息
     */
    private String requestMessage;

    /**
     * 请求的结果
     */
    private String requestStatus;
}