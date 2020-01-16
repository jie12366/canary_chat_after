package com.coder.nettychat.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author monkJay
 * @description
 * @date 2020/1/16 15:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsVO {

    /**
     * 我的账号id
     */
    private String userId;

    /**
     * 好友的账号id
     */
    private String friendId;

    /**
     * 处理的类型
     */
    private Integer type;

    /**
     * 我给好友的备注
     */
    private String friendRemark;

    /**
     * 好友给我的备注
     */
    private String myRemark;
}