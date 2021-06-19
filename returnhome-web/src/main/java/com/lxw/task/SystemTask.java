package com.lxw.task;

import com.lxw.entity.Task;
import com.lxw.system.service.TaskService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 系统定时任务一：
 * 每过半天 查表  (这里用一分钟来代替)
 * 判断任务进行时间和等级 进行操作
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SystemTask implements Job{

    @Autowired
    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("定时任务一开始！");

        //用一分钟和三分钟代替
        long oneDay = 1000 * 60 * 2;
        long threeDay = 1000 * 60 * 5;

        /**
         * 每隔半天遍历所有任务
         * 1.如果有结束时间,说明任务结束就不作任何处理
         * 2.如果等级等于1且任务进行时间大于一天 就进行升级
         * 3.如果等级等于2且任务进行时间大于三天 就进行升级
         */
        List<Task> taskList = taskService.list();
        Date nowTime = new Date();
        for (Task task : taskList) {
            //任务结束了 那就不进行升级
            if (task.getEndTime() != null){
                continue;
            }
            if(task.getLevel() == 1 &&  ((nowTime.getTime() - task.getStartTime().getTime()) > oneDay) ){
                task.setLevel(2);
                taskService.updateById(task);
                continue;
            }
            if(task.getLevel() == 2 && ((nowTime.getTime() - task.getStartTime().getTime()) > threeDay)){
                task.setLevel(3);
                taskService.updateById(task);
            }

        }

        System.out.println("定时任务一执行完毕！");

    }


}
