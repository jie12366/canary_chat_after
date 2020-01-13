package com.coder.nettychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author monkJay
 */
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(ChatApplication.class, args);
//        // 将这个spring容器注入到工具类中
//        SpringUtil.
    }

}
