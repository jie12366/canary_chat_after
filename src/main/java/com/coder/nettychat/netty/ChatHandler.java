package com.coder.nettychat.netty;

import com.coder.nettychat.component.SpringUtil;
import com.coder.nettychat.enums.MsgAction;
import com.coder.nettychat.service.ChatMsgService;
import com.coder.nettychat.utils.JsonUtil;
import com.coder.nettychat.utils.LogUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author monkJay
 * @date 2020/1/7 21:07
 * @description 处理消息的handler
 * TextWebSocketFrame: 在Netty中，专门用于websocket处理文本的对象，frame是消息的载体
 */
class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录和管理所有客户端的channel
     */
    private ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        // 获取客户端传输来的文本消息
        String content = msg.text();
        // 反序列化为MsgContent对象
        MsgContent msgContent = JsonUtil.jsonToPojo(content, MsgContent.class);
        // 获取客户端通道
        Channel channel = ctx.channel();
        // 获取ChatMsgService的Bean对象
        ChatMsgService chatMsgService = (ChatMsgService) SpringUtil.getBean("chatMsgServiceImpl");
        // 获取消息的动作
        Integer action = msgContent.getAction();

        // 1. 当websocket初始化的时候，将客户端用户的id和channel关联起来
        if (action.equals(MsgAction.CONNECT.type)){
            String sendId = msgContent.getChatMsgBo().getSenderId();
            // 绑定客户端id和通道
            UserChannelRel.put(sendId, channel);
        }
        // 2. 聊天信息，把聊天记录保存到数据库，并标记为未签收
        else if (action.equals(MsgAction.CHAT.type)) {
            // 获取消息内容
            ChatMsgBo chatMsgBo = msgContent.getChatMsgBo();
            // 将消息存入数据库，并获取消息的ID
            String msgId = chatMsgService.saveMsg(chatMsgBo);
            chatMsgBo.setMsgId(msgId);

            // 构建一个MsgContent对象发送给接收端
            MsgContent msgContentBo = new MsgContent();
            msgContentBo.setAction(MsgAction.CHAT.type);
            msgContentBo.setChatMsgBo(chatMsgBo);
            // 获取接收者的客户端Channel
            Channel acceptChannel = UserChannelRel.get(chatMsgBo.getReceiverId());
            // 测试通道的打印
            UserChannelRel.output();
            // 如果通道不存在，说明用户不在线
            if (acceptChannel == null){
                // TODO 通过第三方进行消息推送
            } else {
                acceptChannel.writeAndFlush(
                        new TextWebSocketFrame(JsonUtil.convertToJson(msgContentBo))
                );
//                // 当通道存在时，从ChannelGroup中查找是否存在
//                Channel findChannel = users.find(acceptChannel.id());
//                users.writeAndFlush(
//                        new TextWebSocketFrame(JsonUtil.convertToJson(chatMsgBo))
//                );
//                LogUtil.info("用户通道:[{}]", findChannel);
//                // 如果在ChannelGroup中存在，说明用户是在线的
//                if (findChannel != null){
//                    // 给用户发送消息
//                    findChannel.writeAndFlush(
//                            new TextWebSocketFrame(JsonUtil.convertToJson(chatMsgBo))
//                    );
//                }
                // 不存在说明用户也是离线的(伪在线)
//                else {
//                    // TODO 通过第三方进行消息推送
//                }
            }
        }
        // 3. 签收消息，修改数据库信息，标记为签收
        else if (action.equals(MsgAction.SIGNED.type)) {
            // 扩展字段在签收消息中代表要签收的消息ID，消息ID用逗号分隔
            String msgIdsStr = msgContent.getExtend();
            // 分割ids字符串，获取消息ID数组
            String[] ids = msgIdsStr.split(",");
            List<String> msgIdsList = new ArrayList<>();
            for (String mid : ids){
                if (mid != null){
                    msgIdsList.add(mid);
                }
            }
            LogUtil.info(msgIdsList.toString());
            if (msgIdsList.size() > 0){
                // 批量签收消息
                chatMsgService.batchSignMsg(msgIdsList);
            }
        }
        // 4. 保持客户端的心跳
        else if (action.equals(MsgAction.KEEPALIVE.type)) {

        }
    }

    /**
     * 当客户端连接服务端(打开连接)后
     * 获取客户端的channel，并放到ChannelGroup中进行管理
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        users.add(ctx.channel());
    }

    /**
     * 当触发当前方法时，ChannelGroup会自动移除对应客户端的channel
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常时，关闭连接，然后移除客户端的通道
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}