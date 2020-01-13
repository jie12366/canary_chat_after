package com.coder.nettychat.component;

import com.coder.nettychat.utils.LogUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author monkJay
 * @description 获取容器对象，来手动管理bean
 *              使用@Component注册将这个工具类注册到Spring容器中，否则无法完成注入
 * @date 2020/1/12 16:07
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取容器对象
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 通过name来获取注册到spring容器中的bean
     * @param beanName bean的名字
     * @return Object
     */
    public static Object getBean(String beanName){
        return getApplicationContext().getBean(beanName);
    }

    /**
     * 通过class字节码来获取bean
     * @param clazz Class
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        LogUtil.info("容器对象: [{}]", SpringUtil.applicationContext);
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据name和Class来获取bean
     * @param beanName bean的name
     * @param clazz bean的class
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> clazz){
        return getApplicationContext().getBean(beanName, clazz);
    }
}