package com.coder.nettychat.enums;

/**
 * @author monkJay
 * @description 消息的类型
 * @date 2020/1/18 23:30
 */
public enum MsgType {

    /**
     * 消息的类型
     */
    TEXT("text", "文本"),
    IMGAE("image", "图片"),
    AUDIO("audio", "音频");

    public final String type;
    public final String content;

    MsgType(String type, String content) {
        this.type = type;
        this.content = content;
    }
}
