package com.lxw.task;

import com.lxw.system.service.TaskService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统定时任务二：
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SystemTask2 implements Job{

    @Autowired
    private TaskService taskService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

//        System.out.println("定时任务2开始！");
//        /**
//         * 每隔三天遍历所有任务
//         * 1.如果有结束时间,说明任务结束就不作任何处理
//         * 2.如果等级等于2 那就对数据库更新
//         */
//        List<Task> taskList = taskService.list();
//        for (Task task : taskList) {
//            if (task.getEndTime() != null){
//                continue;
//            }
//            if(task.getLevel() == 2){
//                task.setLevel(3);
//                taskService.updateById(task);
//                System.out.println("更新了任务编号（"+task.getTaskId()+"）的等级");
//            }
//        }
//        System.out.println("定时任务2执行完毕！");


    }


}