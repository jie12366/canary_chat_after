package com.coder.nettychat.netty;

import com.coder.nettychat.utils.LogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

/**
 * @author monkJay
 * @description
 * @date 2020/1/5 19:07
 */
@Component
public class WsServer {

    /**
     * 使用静态内部类实现单例模式
     */
    private static class SingletonWsServer{
        static final WsServer INSTANCE = new WsServer();
    }

    public static WsServer getInstance(){
        return SingletonWsServer.INSTANCE;
    }

    private ServerBootstrap sb;

    public WsServer(){
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        EventLoopGroup subGroup = new NioEventLoopGroup();
        sb = new ServerBootstrap();

        sb.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // websocket是基于http协议的，所以需要使用http编解码器
                        pipeline.addLast(new HttpServerCodec())
                                // 对写大数据流的支持
                                .addLast(new ChunkedWriteHandler())
                                // 对http消息的聚合，聚合成FullHttpRequest或FullHttpResponse
                                // 在Netty的编程中，几乎都会使用到这个handler
                                .addLast(new HttpObjectAggregator(1024 * 64));
                        // 以上三个处理器是对http协议的支持

                        // websocket 服务器处理的协议，并用于指定客户端连接的路由(这里指定的是 /ws)
                        // 该处理器为运行websocket服务器承担了所有繁重的工作
                        // 它会负责websocket的握手以及处理控制帧
                        // websocket的数据传输都是以frames进行的
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

                        // 自定义的处理器，用于处理与客户端的通信
                        pipeline.addLast(new ChatHandler());
                    }
                });
    }

    public void start(){
        ChannelFuture future = sb.bind(888);
        future.addListener((channel) -> {
            if (channel.isSuccess()){
                LogUtil.info("Netty websocket server启动成功, 监听端口:[{}]", future.channel().localAddress());
            }else {
                LogUtil.info("Netty websocket server启动失败");
            }
        });
    }
}