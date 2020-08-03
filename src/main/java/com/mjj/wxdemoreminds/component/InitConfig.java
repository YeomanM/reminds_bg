package com.mjj.wxdemoreminds.component;

import com.mjj.wxdemoreminds.util.ApplicationContextHolder;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitConfig {

    @Autowired
    private MyQuartzScheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
//        MyQuartzScheduler scheduler = (MyQuartzScheduler) ApplicationContextHolder.getBean(MyQuartzScheduler.class);
        scheduler.start();
    }

}
