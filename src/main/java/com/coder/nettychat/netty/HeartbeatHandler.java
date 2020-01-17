package com.coder.nettychat.netty;

import com.coder.nettychat.utils.LogUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author monkJay
 * @description 用于实现Netty的心跳检测
 * @date 2020/1/17 21:09
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 针对客户端，如果在指定时间内没有向服务端发送读写心跳，则主动断开
     * @param ctx ChannelHandlerContext
     * @param evt Object
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 如果evt是空闲状态监听事件
        if (evt instanceof IdleStateEvent) {
            // 强转为IdleStateEvent
            IdleStateEvent event = (IdleStateEvent)evt;
            // 读空闲触发，默认不处理
            if (event.state() == IdleState.READER_IDLE) {
                LogUtil.info("进入读空闲");
            }
            // 写空闲触发，默认不处理
            else if (event.state() == IdleState.WRITER_IDLE) {
                LogUtil.info("进入写空闲");
            }
            // 读写空闲触发，关闭客户端的通道
            else if (event.state() == IdleState.ALL_IDLE) {
                LogUtil.info("进入读写空闲");
                // 关闭不用的Channel，避免资源浪费
                ctx.channel().close();
            }
        }
    }
}