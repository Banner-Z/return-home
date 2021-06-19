package com.lxw.monitor;

import com.lxw.entity.Quartz;
import com.lxw.system.service.QuartzService;
import com.lxw.utils.QuartzUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 当我们重启服务的时候，这个时候内存的里面的定时任务其实全部都被销毁，
 * 因此在应用程序启动的时候，还需要将正常的任务重新加入到服务中
 */
@Component
public class TaskConfigApplicationRunner implements ApplicationRunner {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzService quartzService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Quartz> list = quartzService.list();
        System.out.println("========系统定时任务开启=======");
        if(!CollectionUtils.isEmpty(list)){
            for (Quartz quartz : list) {
                //加载启动类型的定时任务
                if(quartz.getStatus().intValue() == 0){
                    QuartzUtils.createScheduleJob(scheduler,quartz);
                }
//                //加载暂停类型的定时任务
//                if(quartz.getStatus().intValue() == 1){
//                    QuartzUtils.createScheduleJob(scheduler,quartz);
//                    QuartzUtils.pauseScheduleJob(scheduler, quartz.getJobName());
//                }
            }
        }
    }

}
