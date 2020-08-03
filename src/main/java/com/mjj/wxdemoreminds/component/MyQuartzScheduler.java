package com.mjj.wxdemoreminds.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mjj.wxdemoreminds.entity.Reminds;
import com.mjj.wxdemoreminds.repository.RemindsRepository;
import com.mjj.wxdemoreminds.util.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author 冯宇明
 * @version 1.0
 * @date 2020/6/22
 * @desc
 */
@Component
@Scope("singleton")
public class MyQuartzScheduler {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    private static final String JOB_DEFAULT_GROUP_NAME = "JOB_DEFAULT_GROUP_NAME";

    private static final String TRIGGER_DEFAULT_GROUP_NAME = "TRIGGER_DEFAULT_GROUP_NAME";


    @Autowired
    private RemindsRepository repository;

    private Scheduler scheduler;
    private Byte lock = 1;

    public void start() throws SchedulerException {
        if (scheduler == null) {
            synchronized (lock) {
                if (scheduler == null) {
//                    RemindsRepository repository = (RemindsRepository) ApplicationContextHolder.getBean(RemindsRepository.class);
                    List<Reminds> reminds = repository.findRemindsByValidTrue();

                    scheduler = schedulerFactory.getScheduler();
                    reminds.forEach(remind -> {
                        try {
                            addJob(remind);
                        } catch (SchedulerException e) {
                            e.printStackTrace();
                        }
                    });
                    scheduler.start();
                }
            }
        }
    }

    public void addJob(Reminds reminds) throws SchedulerException {

        String jobName = String.valueOf(reminds.getId());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, JOB_DEFAULT_GROUP_NAME);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            JobDetail detail = JobBuilder.newJob(QuartzScheduleJob.class)
                    .withIdentity(jobName, JOB_DEFAULT_GROUP_NAME)
                    .usingJobData("openId", reminds.getOpenId())
                    .usingJobData("time", reminds.getTime()).build();
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
            detail.getJobDataMap().put("time", reminds.getTime());


            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(detail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(reminds.formatTime())).build();

            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    public void deleteJob(Reminds reminds) throws SchedulerException {
        JobKey jobKey = new JobKey(String.valueOf(reminds.getId()), JOB_DEFAULT_GROUP_NAME);
        this.scheduler.deleteJob(jobKey);
    }


    @Getter
    @Setter
    public static class QuartzScheduleJob implements Job {

        private static final Logger log = LoggerFactory.getLogger(QuartzScheduleJob.class);

        String openId;
        String time;

        public QuartzScheduleJob() {
        }

        public QuartzScheduleJob(String openId, String time) {
            this.openId = openId;
            this.time = time;
        }

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//            System.out.println(openId);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "★★★★★★★★★★★");
            String accessToken = this.getAccessToken();

            if (StringUtils.isNotBlank(accessToken)) {
                String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
                Properties params = new Properties();
                params.put("touser", this.openId);
                params.put("access_token", accessToken);
                params.put("template_id", "047SSnaOc69ftjn77MN3lYgMIeRKWhmoxLXM2yhuXdE");
                params.put("miniprogram_state", "developer");
                params.put("page", "/pages/closeAndAgain/closeAndAgain");

                Properties data = new Properties(), temp = new Properties();
                temp.put("value", "打卡");
                data.put("thing1", temp);
                temp = new Properties();
                temp.put("value", "今天(" + this.time + ")");
                data.put("thing2", temp);
                temp = new Properties();
                temp.put("value", "下班打卡");
                data.put("thing3", temp);
                temp = new Properties();
                temp.put("value", "记得打卡");
                data.put("thing4", temp);
                params.put("data", data);
                String source = "";

                Properties header = new Properties();
                header.put("Content-Type", "application/json");

                try {
                    log.info(url);
                    log.info(JSONObject.toJSONString(params));
                    source = HttpAccessor.httpPostRequest(url, header,JSONObject.toJSONString(params), 120000);
                    log.info(source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        public String getAccessToken() {
            String tokenName = "access_token";
            Object temp = CacheWithTimeUtil.get(tokenName);
            if (temp == null) {
                PropertiesUtil propertiesUtil = (PropertiesUtil) ApplicationContextHolder.getBean(PropertiesUtil.class);
                String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                        propertiesUtil.getAppId(), propertiesUtil.getAppSecret());
                String source = "";

                try {
                    source = HttpAccessor.doAttemptHttpGet(url);
                    JSONObject object = JSON.parseObject(source);
                    String accessToken = object.getString("access_token");
                    int expiresIn = object.getInteger("expires_in");
                    CacheWithTimeUtil.set(tokenName, accessToken, expiresIn);
                    return accessToken;
                } catch (Exception e){
                    return "";
                }
            } else {
                return (String) temp;
            }
        }
    }

}
