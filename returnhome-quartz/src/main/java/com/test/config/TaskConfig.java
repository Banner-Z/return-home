//package com.test.config;
//
//import com.lxw.task.TestTask;
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TaskConfig {
//
//    @Bean
//    public JobDetail testQuartz() {
//        return JobBuilder.newJob(TestTask.class)
//                .usingJobData("jobData", "test") //数据
//                .withIdentity("myJob", "myJobGroup") //任务名称及分组
//                .storeDurably()
//                .build();
//    }
//
//    @Bean
//    public Trigger testQuartzTrigger() {
//        return TriggerBuilder.newTrigger()
//                .forJob(testQuartz())
//                .withIdentity("myTrigger", "myTriggerGroup") //触发器名称及分组
//                .startNow() //立即执行
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")) //5秒执行一次
//                .build();
//    }
//
//
//}
