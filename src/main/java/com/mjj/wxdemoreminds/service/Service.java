package com.mjj.wxdemoreminds.service;

import com.mjj.wxdemoreminds.component.MyQuartzScheduler;
import com.mjj.wxdemoreminds.entity.Reminds;
import com.mjj.wxdemoreminds.model.BaseResponse;
import com.mjj.wxdemoreminds.repository.RemindsRepository;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private RemindsRepository repository;

    @Autowired
    private MyQuartzScheduler scheduler;

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse save(Reminds reminds) throws SchedulerException {
        reminds = repository.save(reminds);
        scheduler.addJob(reminds);
        return BaseResponse.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(Long id) throws SchedulerException {
        repository.deleteById(id);
        Reminds r = new Reminds();
        r.setId(id);
        scheduler.deleteJob(r);
        return BaseResponse.success();
    }

    public BaseResponse list(String openId) {
        List<Reminds> reminds = repository.findRemindsByOpenIdOrderByIdDesc(openId);
        return BaseResponse.success(reminds);
    }

}
