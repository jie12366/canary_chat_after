package com.coder.nettychat.component;

import com.coder.nettychat.netty.WsServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author monkJay
 * @description 监听容器初始化完成，启动websocket服务器
 * @date 2020/1/7 21:13
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 系统有两个容器root application context和自己的projectName-servlet context(这是子容器)
        // 因为应用监听器的方法是在容器完成初始化后被调用的，为了防止被调用两次（有两个容器)
        // 所以判断只在root application context初始化后调用逻辑代码
        // 当前容器没有父容器，说明它就是root application context
        if (event.getApplicationContext().getParent() == null){
            // 启动websocket server
            // 这里使用了单例模式
            WsServer.getInstance().start();
        }
    }
}