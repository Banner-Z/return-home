package com.lxw.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxw.converter.ImgConverter;
import com.lxw.converter.TaskConverter;
import com.lxw.converter.VolunteerConverter;
import com.lxw.dto.ReportClueReq;
import com.lxw.dto.TaskVolunteerDTO;
import com.lxw.entity.*;
import com.lxw.response.Result;
import com.lxw.system.service.*;
import com.lxw.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lxw
 * @since 2021-03-16
 */
@Api(tags = "任务接口")
@RestController
@RequestMapping("/task")
@Transactional
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private VolunteerTaskService volunteerTaskService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private LostmanInfoService lostmanInfoService;

    @Autowired
    private TaskClueService taskClueService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private LostmanImgService lostmanImgService;


    /**
     * 查询任务列表 (参数0代表所有任务列表  1代表进行中 2代表结束）
     */
    @ApiOperation("查询任务列表(0:所有 1:进行中 2:结束)")
    @GetMapping("/findAllTask/{id}")
    public Result findAllTask(@PathVariable(value = "id") int id){
        //用于页面展示的任务列表
        List<TaskListVO> taskListVOList = new ArrayList<>();
        //查找所有的任务
        List<Task> taskList = taskService.list();
        System.out.println(taskList);
        for (Task task : taskList) {
            /**
             * 查询进行中的任务  如果有结束时间 就跳过
             * 查询已经结束的任务 如果结束时间为空 就跳过
             * 查询全部任务 那都不跳过
             */
            if(id == 1 && task.getEndTime() != null){
                continue;
            }
            if(id == 2 && task.getEndTime() == null) {
                continue;
            }
            //找到参与这个任务的所有志愿者
            List<TaskVolunteerDTO> volunteers = findVolunteerByTaskId(task.getTaskId());
            //查询老人的大致走失地和坐标信息
            LostmanInfo lostmanInfo = lostmanInfoService.getById(task.getLostmanId());
            //查询该老人对应的家属信息
            Family family = familyService.getById(lostmanInfo.getFamilyId());
            //查询该老人对应的照片(随机取一张脸部照片)
            LostmanImg img = lostmanImgService.getOne(new QueryWrapper<LostmanImg>()
                    .eq("lostman_id",lostmanInfo.getLostmanId())
                    .eq("type",0)
                    .last("LIMIT 1"));
            //将老人走失地和任务的志愿者 封装进视图对象
            TaskListVO taskListVO = TaskConverter.converterToTaskVO(task,lostmanInfo,volunteers,family,img);
            taskListVOList.add(taskListVO);
        }
        return Result.success().data("taskList", taskListVOList);
    }


    /**
     * 根据任务编号查找 此任务中的志愿者们
     * 每个志愿者包含的属性：ID 名字 联系方式 以及当前位置
     */
    public List<TaskVolunteerDTO> findVolunteerByTaskId(Long taskId){
        List<TaskVolunteerDTO> volunteers = new ArrayList<>();
        //查中间表得到志愿者ids
        QueryWrapper<VolunteerTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        List<VolunteerTask> volunteerTaskList = volunteerTaskService.list(queryWrapper);
        //根据志愿者ids去封装任务中的志愿者实体
        volunteerTaskList.forEach(volunteerTask -> {
            Long volunteerId = volunteerTask.getVolunteerId();
            TaskVolunteerDTO dto = volunteerService.findTaskVolunteerById(volunteerId);
            volunteers.add(dto);
        });
        return volunteers;
    }


    /**
     * 上报任务线索
     */
    @ApiOperation("上报任务线索")
    @PostMapping("/clue")
    public Result reportClue(ReportClueReq reportClueReq){
        if(reportClueReq.getClue().trim().isEmpty()){
            return Result.error().message("上传线索不能为空！");
        }
        if(taskService.getById(reportClueReq.getTaskId()) == null){
            return Result.error().message("不存在该任务！");
        }
        TaskClue taskClue = new TaskClue();
        BeanUtils.copyProperties(reportClueReq,taskClue);
        taskClue.setTimestamp(new Date());
        taskClueService.save(taskClue);
        return Result.success().message("上报线索成功");
    }


    /**
     * 查看任务线索
     */
    @ApiOperation("查看任务线索")
    @GetMapping("/clue")
    public Result getClue(@RequestParam(value = "taskId",required = true) Long taskId){
        QueryWrapper<TaskClue> queryWrapper = new QueryWrapper();
        queryWrapper.eq("task_id",taskId);
        //查到线索集合
        List<TaskClue> clueList = taskClueService.list(queryWrapper);
        //声明一个线索视图对象的集合
        List<TaskClueVO> clueVOS = new ArrayList<>();
        //封装成视图对象 再发送给前端
        for (TaskClue taskClue : clueList) {
            Long volId = taskClue.getVolunteerId();
            String volunteerName;
            if(volId == 0){
                volunteerName = "指挥系统";
            }else if(volId == -1) {
                volunteerName = "家属";
            }else {
                volunteerName = volunteerService.findVolNameById(volId);
            }
            TaskClueVO clueVO = TaskConverter.converterToTaskClueVO(taskClue,volunteerName);
            clueVOS.add(clueVO);
        }
        return Result.success().data("clueList",clueVOS);
    }


    /**
     * 结束任务
     */
    @ApiOperation("结束任务")
    @PutMapping("/end")
    public Result endTask(@RequestParam("taskId") Long taskId,
                          @RequestParam("rescuePos") String rescuePos){
        Task task = taskService.getById(taskId);
        if(task == null){
            return Result.error().message("该任务不存在！");
        }
        //结束任务之前得做一下判断 该任务是否已经结束
        if(task.getEndTime() != null){
            return Result.error().message("该任务已经结束！");
        }
        Date endTime = new Date();
        task.setEndTime(endTime);
        task.setRescuePos(rescuePos);
        taskService.updateById(task);
        /**
         *  结束任务后 我们得查看中间表
         *  给他们每个志愿者的状态进行-1 （类比计数器）
         *  其实这也可以写在定时任务里面  定期去查看志愿者是否有任务 更新状态
         *  除此之外 还得更新走失老人信息表的走失状态
         */
        List<VolunteerTask> volunteerTaskList = volunteerTaskService.list(
                new QueryWrapper<VolunteerTask>().eq("task_id",taskId));
        Volunteer volunteer;
        for (VolunteerTask volunteerTask : volunteerTaskList) {
            volunteer = volunteerService.getById(volunteerTask.getVolunteerId());
            volunteer.setStatus(volunteer.getStatus()-1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //更新走失状态
        LostmanInfo lostman = new LostmanInfo();
        lostman.setLostmanId(task.getLostmanId());
        lostman.setStatus(1);
        lostmanInfoService.updateById(lostman);

        return Result.success().message("任务已成功结束").data("结束时间",sdf.format(endTime));
    }


    /**
     * 历史任务详细信息
     */
    @ApiOperation("查看历史任务的详细信息")
    @GetMapping("/getPastTaskDetails/{taskId}")
    public Result getPastTaskDetails(@PathVariable("taskId") Long taskId){
        //封装任务详情信息
        Task task = taskService.getById(taskId);
        if(task == null){
            System.out.println("该任务不存在！");
        }
        LostmanInfo lostman = lostmanInfoService.getById(task.getLostmanId());
        Family family = familyService.getById(lostman.getFamilyId());
        PastTaskDetailsVO taskDetailVO = VolunteerConverter.converterToPastTaskDetailsVO(task, lostman, family);

        //封装志愿者简单信息集合(查询中间表)
        List<VolunteerTask> volunteerTaskList = volunteerTaskService.list(new QueryWrapper<VolunteerTask>()
                .eq("task_id", taskId));
        List<VolunteerEasyInfoVO> volunteerVOS = new ArrayList<>();
        Volunteer volunteer;
        VolunteerEasyInfoVO volunteerVO;
        for (VolunteerTask volunteerTask : volunteerTaskList) {
            volunteer = volunteerService.getById(volunteerTask.getVolunteerId());
            volunteerVO = new VolunteerEasyInfoVO();
            BeanUtils.copyProperties(volunteer,volunteerVO);
            volunteerVOS.add(volunteerVO);
        }

        //封装老人所有照片
        List<LostmanImg> imgList = lostmanImgService.list(new QueryWrapper<LostmanImg>()
                .eq("lostman_id", lostman.getLostmanId()));
        List<ImgVO> imgVOS = ImgConverter.converterToImgVOList(imgList);

        return Result.success().data("details",taskDetailVO).data("volunteerList",volunteerVOS).data("imgList",imgVOS);

    }







}

