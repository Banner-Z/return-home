package com.lxw.system.controller;

import com.alibaba.fastjson.JSON;
import com.lxw.response.Result;
import com.lxw.entity.Quartz;
import com.lxw.system.service.QuartzService;
import com.lxw.utils.QuartzUtils;
import io.swagger.annotations.Api;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/quartz")
@Api(tags = "定时任务模块")
public class QuartzController {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzService quartzService;

//    @PostMapping("/test")
//    public Result test(){
//        System.out.println("测试！");
//        return Result.success().message("测试成功！");
//    }
//
//
//    /**
//     * 创建任务
//     */
//    @PostMapping("/createJob")
//    public Result createJob(@RequestBody Quartz quartz)  {
//        System.out.println("test---"+quartz);
//        QuartzUtils.createScheduleJob(scheduler,quartz);
//        quartz.setId(UUID.randomUUID().toString());
//        quartz.setStatus(0);
//        System.out.println("要持久化的quartz类"+quartz);
//        quartzService.save(quartz);
//        System.out.println("=========创建任务成功，信息：{}"+JSON.toJSONString(quartz));
//        return Result.success();
//    }
//
//    /**
//     * 删除定时任务
//     */
//    @DeleteMapping("/delete")
//    public Result delete(@RequestBody Quartz quartz)  {
//        System.out.println("=========删除定时任务，请求参数：{}"+JSON.toJSONString(quartz));
//        QuartzUtils.deleteScheduleJob(scheduler,quartz.getJobName());
//        System.out.println("=========删除定时任务成功=========");
//        return Result.success();
//    }

//    /**
//     * 更新定时任务
//     */
//    @PutMapping("/update")
//    public Result update(@RequestBody Quartz quartz)  {
//        System.out.println("======更新定时任务"+JSON.toJSONString(quartz));
//        QuartzUtils.updateScheduleJob(scheduler,quartz);
//        quartzService.updateById(quartz);
//        System.out.println("=========更新定时任务成功=========");
//        return Result.success();
//    }




//    /**
//     * 暂停任务
//     */
//    @RequestMapping("/pauseJob")
//    public Result pauseJob(@RequestBody Quartz quartz) {
//        log.info("=========开始暂停任务，请求参数：{}=========", JSON.toJSONString(quartz));
//        QuartzUtils.pauseScheduleJob(scheduler, quartz.getJobName());
//        quartzService.update()
//        repository.updateState(quartz.getId(), 1);
//        log.info("=========暂停任务成功=========");
//        return Result.success();
//    }

//    /**
//     * 立即运行一次定时任务
//     */
//    @RequestMapping("/runOnce")
//    public HttpStatus runOnce(@RequestBody Quartz quartz) {
//        log.info("=========立即运行一次定时任务，请求参数：{}", JSON.toJSONString(quartz));
//        QuartzUtils.runOnce(scheduler,quartz.getJobName());
//        log.info("=========立即运行一次定时任务成功=========");
//        return HttpStatus.OK;
//    }

//    /**
//     * 恢复定时任务
//     */
//    @RequestMapping("/resume")
//    public HttpStatus resume(@RequestBody Quartz quartz) {
//        log.info("=========恢复定时任务，请求参数：{}", JSON.toJSONString(quartz));
//        QuartzUtils.resumeScheduleJob(scheduler,quartz.getJobName());
//        repository.updateState(quartz.getId(), 0);
//        log.info("=========恢复定时任务成功=========");
//        return HttpStatus.OK;
//    }




}

