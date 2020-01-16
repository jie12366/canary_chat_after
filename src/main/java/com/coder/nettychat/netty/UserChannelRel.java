package com.coder.nettychat.netty;

import com.coder.nettychat.utils.LogUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 15:55
 */
public class UserChannelRel {

    private static HashMap<String, Channel> manager = new HashMap<>(16);

    /**
     * 将发送者id和通道作为键值对放入map中
     * @param sendId 发送者id
     * @param channel 通道
     */
    public static void put(String sendId, Channel channel){
        manager.put(sendId, channel);
    }

    /**
     * 取出用户id对应的通道
     * @param sendId 发送者id
     * @return 客户端通道
     */
    public static Channel get(String sendId){
        return manager.get(sendId);
    }

    public static void output(){
        for (Map.Entry<String, Channel> map : manager.entrySet()) {
            LogUtil.info("用户:[{}], 通道:[{}]", map.getKey(), map.getValue());
        }
    }
}