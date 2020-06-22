package com.mjj.wxdemoreminds.component;

import com.mjj.wxdemoreminds.entity.Reminds;
import com.mjj.wxdemoreminds.repository.RemindsRepository;
import com.mjj.wxdemoreminds.util.ApplicationContextHolder;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/6/22
 * @desc
 */
@Component
@Scope("singleton")
public class QuartzScheduler {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    private static final String JOB_DEFAULT_GROUP_NAME = "JOB_DEFAULT_GROUP_NAME";

    private static final String TRIGGER_DEFAULT_GROUP_NAME = "TRIGGER_DEFAULT_GROUP_NAME";

    private Scheduler scheduler;
    private Byte lock = 1;

    public void start() throws SchedulerException {
        if (scheduler == null) {
            synchronized (lock) {
                if (scheduler == null) {
                    RemindsRepository repository = (RemindsRepository) ApplicationContextHolder.getBean(RemindsRepository.class);
                    List<Reminds> reminds = repository.findRemindsByValidTrue();

                    scheduler = schedulerFactory.getScheduler();
                    reminds.forEach(remind -> {

                    });
                }
            }
        }
    }

    public void addJob(Reminds reminds) throws SchedulerException {
        QuartzScheduleJob job = new QuartzScheduleJob(reminds.getOpenId(), reminds.getType());

        String jobName = String.valueOf(reminds.getId());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, JOB_DEFAULT_GROUP_NAME);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            JobDetail detail = JobBuilder.newJob(QuartzScheduleJob.class)
                    .withIdentity(jobName, JOB_DEFAULT_GROUP_NAME)
                    .usingJobData("openId", reminds.getOpenId())
                    .usingJobData("type", reminds.getOpenId()).build();
            CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(reminds.formatTime());

            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, JOB_DEFAULT_GROUP_NAME).withSchedule(builder).build();
            scheduler.scheduleJob(detail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            modifyJob(reminds);
        }
    }

    public void modifyJob(Reminds reminds) throws SchedulerException {
        String jobName = String.valueOf(reminds.getId());
        JobKey key = new JobKey(jobName, JOB_DEFAULT_GROUP_NAME);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, JOB_DEFAULT_GROUP_NAME);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            JobDetail detail = scheduler.getJobDetail(key);
            detail.getJobDataMap().put("openId", reminds.getOpenId());
            detail.getJobDataMap().put("type", reminds.getOpenId());

            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(detail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(reminds.formatTime())).build();

            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }


    private class QuartzScheduleJob implements Job {

        String openId;
        int type;

        public QuartzScheduleJob() {
        }

        public QuartzScheduleJob(String openId, int type) {
            this.openId = openId;
            this.type = type;
        }

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//            System.out.println(openId);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "★★★★★★★★★★★");
        }
    }

}
