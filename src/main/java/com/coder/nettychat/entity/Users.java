package com.coder.nettychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author monkJay
 * @date 2020/1/7 19:44
 * @description 用户账号
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    /**
     * 账号id
     */
    @Id
    @NotNull
    private String id;

    /**
     * 账号名
     */
    private String username;

    /**
     * 账号密码
     */
    private String password;

    /**
     * 账号头像
     */
    private String faceImage;

    /**
     * 账号头像大图
     */
    private String faceImageBig;

    /**
     * 账号昵称
     */
    private String nickname;

    /**
     * 账号二维码
     */
    private String qrcode;

    /**
     * 账号的客户端(设备)id
     */
    private String cid;
}