package com.lxw.utils;

import com.lxw.entity.Quartz;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

/**
 * 系统任务的辅助类
 */
@Slf4j
public class QuartzUtils {

    /**
     * 创建定时任务 定时任务创建之后默认启动状态
     * @param scheduler 调度器
     * @param quartz  定时任务信息类
     */
    public static void createScheduleJob(Scheduler scheduler, Quartz quartz){
        try {
            //获取到定时任务的执行类  必须是类的绝对路径名称
            //定时任务类需要是job类的具体实现 QuartzJobBean是job的抽象类。
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName("com.lxw.task."+quartz.getJobClass());
            // 构建定时任务信息
            JobDetail jobDetail = JobBuilder
                    .newJob(jobClass)
                    .build();
            // 设置定时任务执行方式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
            // 构建触发器trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity(quartz.getJobName(),"triggerGroup"+quartz.getTaskId())
                    .withSchedule(scheduleBuilder)
                    .forJob(jobDetail)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            log.error("定时任务类路径出错：请输入类的绝对路径", e);
        } catch (SchedulerException e) {
            log.error("创建定时任务出错", e);
        }
    }


    /**
     * 根据定时任务名称从调度器当中删除定时任务
     * @param scheduler 调度器
     * @param jobName   定时任务名称
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("删除定时任务出错", e);
        }
    }

    /**
     * 更新定时任务
     * @param quartz  定时任务信息类
     */
    public static void updateScheduleJob(Quartz quartz,JobExecutionContext context)  {
        try {
            //获取到对应任务的触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName());

            //重新构建任务的触发器trigger
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
            // 构建触发器trigger
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withSchedule(scheduleBuilder)
                    .forJob(context.getJobDetail())
                    .build();
            //重置对应的job
            context.getScheduler().rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务出错", e);
        }
    }

//    /**
//     * 根据任务名称暂停定时任务
//     * @param scheduler  调度器
//     * @param jobName    定时任务名称
//     */
//    public static void pauseScheduleJob(Scheduler scheduler, String jobName){
//        JobKey jobKey = JobKey.jobKey(jobName);
//        try {
//            scheduler.pauseJob(jobKey);
//        } catch (SchedulerException e) {
//            log.error("暂停定时任务出错", e);
//        }
//    }
//
//    /**
//     * 根据任务名称恢复定时任务
//     * @param scheduler  调度器
//     * @param jobName    定时任务名称
//     */
//    public static void resumeScheduleJob(Scheduler scheduler, String jobName) {
//        JobKey jobKey = JobKey.jobKey(jobName);
//        try {
//            scheduler.resumeJob(jobKey);
//        } catch (SchedulerException e) {
//            log.error("暂停定时任务出错", e);
//        }
//    }
//
//    /**
//     * 根据任务名称立即运行一次定时任务
//     * @param scheduler     调度器
//     * @param jobName       定时任务名称
//     */
//    public static void runOnce(Scheduler scheduler, String jobName){
//        JobKey jobKey = JobKey.jobKey(jobName);
//        try {
//            scheduler.triggerJob(jobKey);
//        } catch (SchedulerException e) {
//            log.error("运行定时任务出错", e);
//        }
//    }
//





}

