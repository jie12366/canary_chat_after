package com.coder.nettychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @author monkJay
 * @date 2020/1/7 19:44
 * @description 我的好友
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFriends {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 我的账号id
     */
    private String myUserId;

    /**
     * 我好友的账号id
     */
    private String myFriendUserId;

    /**
     * 好友的备注
     */
    private String friendRemark;
}