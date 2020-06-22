package com.mjj.wxdemoreminds.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/6/22
 * @desc
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBean(String className) {
        return context.getBean(className);
    }

    public static Object getBean(Class clazz) {
        return context.getBean(clazz);
    }
}
