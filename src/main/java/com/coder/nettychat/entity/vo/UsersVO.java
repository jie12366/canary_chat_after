package com.coder.nettychat.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author monkJay
 * @date 2020/1/7 19:44
 * @description 用于返回给前端的组合类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersVO {
    private String id;
    private String username;
    private String faceImage;
    private String faceImageBig;
    private String nickname;
    private String qrcode;
}