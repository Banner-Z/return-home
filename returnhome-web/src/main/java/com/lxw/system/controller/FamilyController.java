package com.lxw.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxw.converter.FamilyConverter;
import com.lxw.converter.ImgConverter;
import com.lxw.dto.PerfectLostmanReq;
import com.lxw.dto.ReportLostmanReq;
import com.lxw.dto.SystemReportLostmanReq;
import com.lxw.entity.*;
import com.lxw.response.Result;
import com.lxw.system.service.*;
import com.lxw.utils.GaoDeMapUtils;
import com.lxw.utils.LocationUtils;
import com.lxw.utils.SendSMSUtils;
import com.lxw.vo.FamilyTaskDetailsVO;
import com.lxw.vo.ImgVO;
import com.lxw.vo.ReportTaskRecordsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(tags = "家属端模块")
@RestController
@RequestMapping("/family")
@Transactional
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private LostmanInfoService lostmanInfoService;

    @Autowired
    private VolunteerPosService volunteerPosService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private LostmanImgService lostmanImgService;


    /**
     * 家属上报走失老人信息(步骤较多得注意）
     * 1.保存家属信息
     * 2.保存老人信息
     * 3.寻找符合范围的志愿者 并且创建任务
     * 4.将该任务加入到系统定时任务列表中 方便任务降级
     */
    @ApiOperation("上报走失老人信息,创建任务")
    @PostMapping("/report")
    public Result report(ReportLostmanReq reportLostmanReq){
        //保存家属信息
        Family family = new Family(reportLostmanReq.getOpenid(),reportLostmanReq.getFamilyName(),
                reportLostmanReq.getFamilyPhoneNumber());
        familyService.save(family);
        Long familyId = family.getFamilyId();

        //保存老人信息
        LostmanInfo lostmanInfo = new LostmanInfo();
        BeanUtils.copyProperties(reportLostmanReq,lostmanInfo);
        lostmanInfo.setFamilyId(familyId);
        lostmanInfo.setStatus(0);

        //寻找符合范围的志愿者 创建任务
        if(lostmanInfoService.save(lostmanInfo)){
            System.out.println("老人走失位置: "+lostmanInfo.getLostmanPos());
            List<String> openIds = this.findNearestVolunteer(lostmanInfo.getLostmanPosLongitude(),lostmanInfo.getLostmanPosLatitude());
            //创建一个任务 等待志愿者加入
            Task task = new Task();
            task.setLostmanId(lostmanInfo.getLostmanId());
            task.setStartTime(lostmanInfo.getLostTime());
            taskService.save(task);
            SendSMSUtils.sendMsg((long) 27,"18606699851",task.getTaskId());
            return Result.success().message("上报老人信息成功！").data("volunteerOpenIds",openIds).data("taskId",task.getTaskId()).data("lostmanId",lostmanInfo.getLostmanId());
        }
        return Result.error().message("上报老人信息失败，请稍后再试！");
    }


    /**
     * 查找最近的符合条件的志愿者
     */
    public List<String> findNearestVolunteer(String longitude,String latitude){
        List<String> openIds = new ArrayList<>();
        String flag = "成功找到志愿者"; //用来做测试
        int range = 5000; //搜救范围
        Double x = Double.parseDouble(longitude);
        Double y = Double.parseDouble(latitude);
        System.out.println("成功上报老人的坐标：x: "+x+" y: "+y);
        //这边默认报给我们的老人坐标是正确 1.查询符合坐标范围的志愿者 2.查询志愿者
        //中国的经纬度范围大约为：纬度3~54，经度73~136
        List<VolunteerPos> volunteerPosList = volunteerPosService.list();
        for (VolunteerPos volunteerPos : volunteerPosList) {
            Double lng = Double.parseDouble(volunteerPos.getLongitude());//经度
            Double lat = Double.parseDouble(volunteerPos.getLatitude());//纬度
            System.out.println(volunteerPos.getVolunteerId()+" 号志愿者的经纬是"+lng+" "+lat);
            //是否在中国
            if(lng>73&&lng<136&&lat>3&&lat<54){
                //计算老人位置和志愿者位置的距离
                Double distance = LocationUtils.getDistance(x,y,lng,lat);
                System.out.println("距离"+distance);
                //如果志愿者在范围内,就查找openId 添加到集合中去
                if(distance <= range){
                    Volunteer volunteer = volunteerService.getById(volunteerPos.getVolunteerId());
                    openIds.add(volunteer.getOpenid());
                }
            }
        }

        //如果全部志愿者都遍历完了,还是没有符合要求的志愿者---那我们就扩大范围
        if(openIds.size() == 0){
            flag = "5km范围内没有搜寻到符合要求的志愿者,已扩大到10km";
            range += 5000;
            for (VolunteerPos volunteerPos : volunteerPosList) {
                Double lng = Double.parseDouble(volunteerPos.getLongitude());//经度
                Double lat = Double.parseDouble(volunteerPos.getLatitude());//纬度
                System.out.println(volunteerPos.getVolunteerId()+" 号志愿者的经纬是"+lng+" "+lat);
                //是否在中国
                if(lng>73&&lng<136&&lat>3&&lat<54){
                    //计算老人位置和志愿者位置的距离
                    Double distance = LocationUtils.getDistance(x,y,lng,lat);
                    System.out.println(" 距离："+distance);
                    //如果志愿者在范围内,就查找openId 添加到集合中去
                    if(distance <= range){
                        Volunteer volunteer = volunteerService.getById(volunteerPos.getVolunteerId());
                        if(volunteer.getStatus()!= -1){
                            openIds.add(volunteer.getOpenid());
                        }
                    }
                }
            }
        }

        System.out.println(flag);
        openIds.add(String.valueOf(range));
        return openIds;

    }


    /**
     * 指挥系统的工作人员辅助家属上报走失老人信息
     * 原理同上  只不过表单接收的数据多一点（详细一点） 缺少家属的openid
     * 多一个转换详细地址为经纬度的操作（工作人员一般能保证输入地址是正确的）
     */
    @ApiOperation("指挥系统辅助上报走失老人信息")
    @PostMapping("/SysHelpReport")
    public Result SysReport(SystemReportLostmanReq req){
        //保存家属信息
        Family family = new Family();
        family.setFamilyName(req.getFamilyName());
        family.setFamilyPhoneNumber(req.getFamilyPhoneNumber());
        familyService.save(family);
        Long familyId = family.getFamilyId();

        //保存老人信息
        LostmanInfo lostmanInfo = new LostmanInfo();
        BeanUtils.copyProperties(req,lostmanInfo);
        lostmanInfo.setFamilyId(familyId);
        lostmanInfo.setStatus(0);
        //地址转换 得到经纬度
        JSONObject lngAndLat = GaoDeMapUtils.getLngAndLat(req.getLostmanPos());
        lostmanInfo.setLostmanPosLongitude(lngAndLat.getString("longitude"));
        lostmanInfo.setLostmanPosLatitude(lngAndLat.getString("latitude"));

        //寻找符合范围的志愿者 创建任务
        if(lostmanInfoService.save(lostmanInfo)){
            System.out.println("老人走失位置: "+lostmanInfo.getLostmanPos());
            List<String> openIds = this.findNearestVolunteer(lostmanInfo.getLostmanPosLongitude(),lostmanInfo.getLostmanPosLatitude());
            //创建一个任务 等待志愿者加入
            Task task = new Task();
            task.setLostmanId(lostmanInfo.getLostmanId());
            task.setStartTime(lostmanInfo.getLostTime());
            taskService.save(task);
            return Result.success().message("上报老人信息成功！").data("volunteerOpenIds",openIds).data("taskId",task.getTaskId()).data("lostmanId",lostmanInfo.getLostmanId());
        }
        return Result.error().message("上报老人信息失败，请稍后再试！");
    }

    
    /**
     * 完善走失老人信息
     */
    @ApiOperation("完善走失老人信息")
    @PutMapping("/perfect")
    public Result perfect(PerfectLostmanReq perfectLostmanReq){
        LostmanInfo lostmanInfo = new LostmanInfo();
        BeanUtils.copyProperties(perfectLostmanReq,lostmanInfo);
        if(lostmanInfoService.updateById(lostmanInfo)){
            return Result.success().message("完善老人信息成功！");
        }
        return Result.error().message("完善老人信息失败,请稍后再试！");
    }


    /**
     * 查询家属过往上报记录
     */
    @ApiOperation("查询家属过往上报记录")
    @GetMapping("/getReportRecords")
    public Result getReportRecords(@RequestParam("openid") String openid,
                                   @RequestParam(value = "lostmanName",required = false,defaultValue = "")String lostmanName){
        QueryWrapper<Family> wrapper = new QueryWrapper();
        wrapper.eq("openid",openid);
        List<Family> familyList = familyService.list(wrapper);
        List<ReportTaskRecordsVO> voList = new ArrayList<>();
        for (Family family : familyList) {
            LostmanInfo lostman = lostmanInfoService.getOne(new QueryWrapper<LostmanInfo>()
                    .eq("family_id", family.getFamilyId())
                    .like("lostman_name",lostmanName)
            );
            if(lostman == null){
                continue;
            }
            Task task = taskService.getOne(new QueryWrapper<Task>()
                    .eq("lostman_id",lostman.getLostmanId()));
            ReportTaskRecordsVO vo = FamilyConverter.converterToReportTaskRecordsVO(lostman, task);
            voList.add(vo);
        }
        return Result.success().data("records",voList);
    }



    /**
     * 家属端查看任务详细信息
     */
    @ApiOperation("查看任务详细信息")
    @GetMapping("/taskDetails")
    public Result getTaskDetails(Long taskId){
        Task task = taskService.getById(taskId);
        if(task == null){
            return Result.error().message("该任务不存在！");
        }
        //任务详情的VO
        LostmanInfo lostman = lostmanInfoService.getById(task.getLostmanId());
        Family family = familyService.getById(lostman.getFamilyId());
        FamilyTaskDetailsVO taskVO = FamilyConverter.converterToFamilyTaskVO(lostman,family);
        //照片的VOList
        QueryWrapper<LostmanImg> wrapper = new QueryWrapper<>();
        wrapper.eq("lostman_id",lostman.getLostmanId());
        List<LostmanImg> imgList = lostmanImgService.list(wrapper);
        List<ImgVO> imgVOS = ImgConverter.converterToImgVOList(imgList);
        return Result.success().data("details",taskVO).data("imgList",imgVOS);
    }





}

