package com.lxw.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.converter.ImgConverter;
import com.lxw.converter.VolunteerConverter;
import com.lxw.dto.*;
import com.lxw.entity.*;
import com.lxw.system.service.*;
import com.lxw.response.Result;
import com.lxw.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  志愿者控制器
 * </p>
 *
 * @author lxw
 * @since 2021-03-08
 */
@RestController
@RequestMapping("/volunteer")
@Api(tags = "志愿者模块")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private VolunteerPosService volunteerPosService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VolunteerTaskService volunteerTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private LostmanInfoService lostmanInfoService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private LostmanImgService lostmanImgService;


    /**
     * 分页查询志愿者列表
     */
    @ApiOperation("分页查询志愿者列表")
    @PostMapping("/findVolunteerList")
    public Result findVolunteerList(FindVolunteerInfoReq findVolunteerInfoReq) {
        Page<Volunteer> page = new Page<>(findVolunteerInfoReq.getPageNum(), findVolunteerInfoReq.getPageSize());
        QueryWrapper<Volunteer> wrapper = getWrapper(findVolunteerInfoReq);
        IPage<Volunteer> userPage = volunteerService.findVolunteerPage(page, wrapper);
        long total = userPage.getTotal();
        List<Volunteer> volunteers = userPage.getRecords();
        //这里拿到的records 全部字段都有 但是我们不用全部展示到前端 所以可以转成VO再放进去
        List<VolunteerInfoVO> records = VolunteerConverter.converterToVolunteerInfoVOList(volunteers);
        return Result.success().data("total", total).data("records", records);
    }


    /**
     * 封装分页的查询条件
     */
    private QueryWrapper<Volunteer> getWrapper(FindVolunteerInfoReq findVolunteerInfoReq) {
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(findVolunteerInfoReq.getVolunteerId())){
            queryWrapper.eq("volunteer_id", findVolunteerInfoReq.getVolunteerId());
        }
        if (!StringUtils.isEmpty(findVolunteerInfoReq.getVolunteerName())) {
            queryWrapper.like("volunteer_name", findVolunteerInfoReq.getVolunteerName());
        }
        if (!StringUtils.isEmpty(findVolunteerInfoReq.getPhoneNumber())) {
            queryWrapper.like("phone_number", findVolunteerInfoReq.getPhoneNumber());
        }
        if (!StringUtils.isEmpty(findVolunteerInfoReq.getSex())) {
            queryWrapper.eq("sex", findVolunteerInfoReq.getSex());
        }
        return queryWrapper;
    }

    /**
     * 查询某个志愿者
     */
    @ApiOperation("查询某个志愿者信息")
    @GetMapping("/findVolunteer/{id}")
    public Result findVolunteerById(@PathVariable("id") Long volunteerId){
        Volunteer volunteer = volunteerService.getById(volunteerId);
        if(volunteer == null){
            return Result.error().message("该志愿者不存在");
        }
        VolunteerInfoVO vo = new VolunteerInfoVO();
        BeanUtils.copyProperties(volunteer,vo);
        return Result.success().data("volunteer",vo);
    }


    /**
     * 更新志愿者当前位置
     */
    @ApiOperation("更新志愿者当前位置")
    @PutMapping("/pos")
    public Result updatePos(VolunteerPosReq volunteerPosReq){
        VolunteerPos volunteerPos = new VolunteerPos();
        //确保插入的志愿者 ID 是存在的
        if(volunteerService.getById(volunteerPosReq.getVolunteerId()) == null){
            return Result.error().message("该志愿者不存在，请确保数据正确!");
        }
        BeanUtils.copyProperties(volunteerPosReq,volunteerPos);
        volunteerPosService.saveOrUpdate(volunteerPos);
        return Result.success().message("更新志愿者位置成功!");
    }


    /**
     * 志愿者确认加入任务
     */
    @ApiOperation("志愿者确认加入任务")
    @PostMapping("/join")
    public Result joinTask(@RequestParam("taskId") Long taskId,
                           @RequestParam(value = "volunteerId",required = false) Long volunteerId,
                           @RequestParam(value = "openid",required = false)String openid){
        Volunteer volunteer;
        //方便小程序端(当两个参数同时存在时  以openid为优先)
        if(openid != null && !openid.isEmpty()){
            volunteer = volunteerService.getOne(new QueryWrapper<Volunteer>()
                    .eq("openid",openid));
        }else {
            volunteer = volunteerService.getById(volunteerId);
        }

        //志愿者确认加入任务,首先得更新一下志愿者的status
        if(volunteer == null){
            return Result.error().message("该志愿者不存在");
        }
        //判断志愿者是否在任务中
        List<VolunteerTask> volTaskList = volunteerTaskService.list(new QueryWrapper<VolunteerTask>()
                .eq("task_id", taskId));
        for (VolunteerTask volTask : volTaskList) {
            if(volTask.getVolunteerId() == volunteer.getVolunteerId()){
                return Result.error().message("该志愿者已经在任务中，不能重复加入！");
            }
        }

        volunteer.setStatus(volunteer.getStatus()+1);
        volunteerService.updateById(volunteer);
        //查看是否有该任务
        Task task = taskService.getById(taskId);
        if(task == null){
            return Result.error().message("不存在该任务");
        }

        //插入中间表
        VolunteerTask volunteerTask = new VolunteerTask();
        volunteerTask.setTaskId(taskId);
        volunteerTask.setVolunteerId(volunteer.getVolunteerId());
        volunteerTaskService.save(volunteerTask);
        String template = String.format("%s号志愿者，欢迎您加入%s号任务!",volunteer.getVolunteerId(),taskId);
        return Result.success().message(template);
    }



    /**
     * 志愿者端 查看该志愿者的任务列表 方便进行线索提交
     * 一个志愿者可以接多个任务  要查询中间表得到任务ID集合
     * 记得判断一下 任务集合是否为空 因为该志愿者可能没加入任务
     */
    @ApiOperation("查看该志愿者的任务列表")
    @GetMapping("/getTaskList")
    public Result getTaskList(String openid){
        //志愿者
        Volunteer volunteer = volunteerService.getOne(new QueryWrapper<Volunteer>()
                .eq("openid",openid));
        if(volunteer == null){
            return Result.error().message("该志愿者不存在！");
        }
        //根据志愿者id查询中间表 得到集合
        List<VolunteerTask> volunteerTaskList = volunteerTaskService.list(new QueryWrapper<VolunteerTask>()
                .eq("volunteer_id", volunteer.getVolunteerId()));
        if(volunteerTaskList.size() == 0){
            return Result.error().message("该志愿者还没加入任何任务！");
        }
        List<Task> taskList = new ArrayList<>();
        volunteerTaskList.forEach(volunteerTask -> {
            Task task = taskService.getById(volunteerTask.getTaskId());
            taskList.add(task);
        });
        List<VolunteerClueTaskDTO> dtos = new ArrayList<>();
        for (Task task : taskList) {
            VolunteerClueTaskDTO dto = new VolunteerClueTaskDTO();
            dto.setTaskId(task.getTaskId());
            LostmanInfo lostman = lostmanInfoService.getById(task.getLostmanId());
            dto.setLostmanName(lostman.getLostmanName());
            dtos.add(dto);
        }
        VolunteerClueTaskVO vo = VolunteerConverter.converterToVolunteerTaskListVO(volunteer,dtos);
        return Result.success().data("taskList",vo);
    }


    /**
     * 志愿者端查看某个任务详细信息
     */
    @ApiOperation("查看某个任务的详细信息")
    @GetMapping("/taskDetails")
    public Result getTaskDetails(Long taskId){
        Task task = taskService.getById(taskId);
        if(task == null){
            return Result.error().message("该任务不存在！");
        }
        //任务详情的VO
        LostmanInfo lostman = lostmanInfoService.getById(task.getLostmanId());
        Family family = familyService.getById(lostman.getFamilyId());
        VolunteerTaskDetailsVO taskVO = VolunteerConverter.converterToVolunteerTaskDetailsVO(lostman,family,task);
        //照片的VOList
        QueryWrapper<LostmanImg> wrapper = new QueryWrapper<>();
        wrapper.eq("lostman_id",lostman.getLostmanId());
        List<LostmanImg> imgList = lostmanImgService.list(wrapper);
        List<ImgVO> imgVOS = ImgConverter.converterToImgVOList(imgList);
        //该任务志愿者的编号和姓名
        List<Volunteer> volunteerList = new ArrayList<>();
        //中间表
        List<VolunteerTask> volunteerTaskList = volunteerTaskService.list(new QueryWrapper<VolunteerTask>()
                .eq("task_id", taskId));
        volunteerTaskList.forEach(volunteerTask -> {
            Volunteer volunteer = volunteerService.getById(volunteerTask.getVolunteerId());
            volunteerList.add(volunteer);
        });
        List<VolunteerEasyInfoVO> volunteerVOS = VolunteerConverter.converterToVolunteerEasyInfoVOList(volunteerList);
        return Result.success().data("details",taskVO)
                .data("imgList",imgVOS).data("volunteerList",volunteerVOS);
    }


    /**
     * 查看志愿者当前执行中的任务列表
     */
    @ApiOperation("查看志愿者执行中的任务列表")
    @GetMapping("/getActiveTaskList")
    public Result getActiveTaskList(String openid){
        return getTaskList(openid,0);
    }


    /**
     * 查看志愿者历史任务列表
     */
    @ApiOperation("查看志愿者历史任务列表")
    @GetMapping("/getPastTaskList")
    public Result getPastTaskList(String openid){
       return getTaskList(openid,1);
    }


    /**
     * 获取任务列表(执行中的或者历史的)
     * @param i (0表示查看正在进行的 1表示查看历史）
     */
    public Result getTaskList(String openid,int i){
        Volunteer volunteer = volunteerService.getOne(new QueryWrapper<Volunteer>()
                .eq("openid", openid));
        if(volunteer == null){
            return Result.error().message("该志愿者不存在！");
        }
        //查询中间表 看该志愿者有哪些任务
        List<VolunteerTask> volunteerTaskList = volunteerTaskService.list(new QueryWrapper<VolunteerTask>()
                .eq("volunteer_id", volunteer.getVolunteerId()));
        //得到任务列表
        List<Task> taskList = new ArrayList<>();
        for (VolunteerTask volunteerTask : volunteerTaskList) {
            Task task = taskService.getById(volunteerTask.getTaskId());

            //查看正在进行的任务  如果有结束时间就跳过
            if(i == 0 && task.getEndTime() != null){
                continue;
            }
            //查看历史任务  如果没有结束时间就跳过
            if(i == 1 && task.getEndTime() == null){
                continue;
            }
            taskList.add(task);
        }
        //对每个任务进行处理(声明放外面)
        List<VolunteerCheckTaskVO> vos = new ArrayList<>();
        LostmanInfo lostman;
        LostmanImg img;
        VolunteerCheckTaskVO vo;
        for (Task task : taskList) {
            lostman = lostmanInfoService.getById(task.getLostmanId());
            //照片 只要一张脸部的
            img = lostmanImgService.getOne(new QueryWrapper<LostmanImg>()
                    .eq("lostman_id", lostman.getLostmanId())
                    .eq("type", 0)
                    .last("LIMIT 1")
            );
            vo = VolunteerConverter.converterToVolunteerCheckTaskVO(task, lostman, img);
            vos.add(vo);
        }
        return Result.success().data("taskList",vos);
    }


    /**
     * 修改志愿者信息
     */
    @ApiOperation("修改志愿者信息")
    @PostMapping("/updateVolunteerInfo")  //PUT请求提交的表单 tomcat不解析 spring就拿不到
    public Result updateVolunteerInfo(UpdateVolunteerInfoReq req){
        System.out.println("ID"+req.getVolunteerId());
        Volunteer volunteer = volunteerService.getById(req.getVolunteerId());
        if(volunteer == null){
            return Result.error().message("该志愿者不存在");
        }
        BeanUtils.copyProperties(req,volunteer);
        volunteerService.updateById(volunteer);
        return Result.success().message("更新志愿者信息成功！");
    }


    /**
     * 根据志愿者编号获取openid
     */
    @ApiOperation("根据志愿者编号获取openid")
    @GetMapping("/getOpenid")
    public Result getOpenId(Long volunteerId){
        Volunteer volunteer = volunteerService.getById(volunteerId);
        if(volunteer == null){
            return Result.error().message("该志愿者不存在!");
        }
        return Result.success().data("openid",volunteer.getOpenid());
    }


}

