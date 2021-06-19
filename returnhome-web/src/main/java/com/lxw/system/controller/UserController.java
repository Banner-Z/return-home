package com.lxw.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.converter.ImgConverter;
import com.lxw.converter.LostmanConverter;
import com.lxw.dto.AddVolunteerReq;
import com.lxw.dto.FindLostmanReq;
import com.lxw.dto.GetUserListReq;
import com.lxw.entity.*;
import com.lxw.response.Result;
import com.lxw.system.service.*;
import com.lxw.utils.ExportExcelUtils;
import com.lxw.utils.SendSMSUtils;
import com.lxw.vo.FamilyTaskDetailsVO;
import com.lxw.vo.ImgVO;
import com.lxw.vo.UserListVO;
import com.lxw.vo.VolunteerInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  管理员控制器
 * </p>
 */
@Api(tags = "系统管理员模块")
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private LostmanInfoService lostmanInfoService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LostmanImgService lostmanImgService;

    /**
     * 超级管理员
     */
    @ApiOperation("管理员添加管理员")
    @PostMapping("/addUser")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result addUser(@RequestParam("username")String username,
                          @RequestParam("password")String password){
        //先对密码进行加密
        password = passwordEncoder.encode(password);
        User user = new User(username, password, (long) 0);
        userService.save(user);
        //更新到用户权限中间表
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId((long) 2);
        userRoleService.save(userRole);
        return Result.success().message("添加管理员成功");
    }


    /**
     * 管理员添加志愿者
     * 1. 对密码进行加密
     * 2. 添加到系统用户表
     * 3. 更新到用户权限中间表
     * 4. 志愿者数据持久化
     */
    @ApiOperation("管理员添加志愿者")
    @PostMapping("/addVolunteer")
    public Result addVolunteer(AddVolunteerReq addVolunteerReq){
        //先对密码进行加密
        String password = addVolunteerReq.getPassword();
        addVolunteerReq.setPassword(passwordEncoder.encode(password));

        //属性复制
        Volunteer volunteer = new Volunteer();
        BeanUtils.copyProperties(addVolunteerReq,volunteer);
        //加上创建时间
        volunteer.setCreateTime(new Date());
        //判断邮箱是否为空
        if(addVolunteerReq.getEmail() == null || addVolunteerReq.getEmail().isEmpty()){
            volunteer.setEmail("待完善");
        }

        int i = volunteerService.addVolunteer(volunteer);
        //添加到系统用户表
        User user = new User(addVolunteerReq.getAccount(),addVolunteerReq.getPassword(),volunteer.getVolunteerId());
        userService.save(user);
        //更新到用户权限中间表
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId((long) 4); //角色4是志愿者
        userRoleService.save(userRole);

        return i > 0 ? Result.success().message("添加志愿者成功") : Result.error().message("添加志愿者失败");

    }


    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam(value = "openid",required = false)String openid){
        return Result.success();
    }


    /**
     * 导出志愿者信息表 excel
     * 超级管理员才有权限导出
     */
    @ApiOperation(value = "导出志愿者信息表")
    @PostMapping(value = "/volunteerPort")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void volunteerPort(HttpServletResponse response) throws Exception {
        String fileName = URLEncoder.encode("demo", "UTF-8");
        List<Volunteer> volunteerList = volunteerService.list();
        List<VolunteerExcel> excels = new ArrayList<>();
        VolunteerExcel excel;
        for (Volunteer volunteer : volunteerList) {
            excel = new VolunteerExcel();
            BeanUtils.copyProperties(volunteer,excel);
            excels.add(excel);
        }
        ExportExcelUtils.export2Web(response,fileName,"志愿者列表", VolunteerExcel.class,excels);
    }


    /**
     * 导出老人信息表 excel
     * 超级管理员才有权限导出
     */
    @ApiOperation(value = "导出志愿者信息表")
    @PostMapping(value = "/lostmanPort")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void lostmanPort(HttpServletResponse response) throws Exception {
        String fileName = URLEncoder.encode("demo", "UTF-8");
        List<LostmanInfo> lostmanList = lostmanInfoService.list();
        List<LostmanExcel> excels = new ArrayList<>();
        LostmanExcel excel;
        for (LostmanInfo lostman : lostmanList) {
            excel = new LostmanExcel();
            BeanUtils.copyProperties(lostman,excel);
            excels.add(excel);
        }
        ExportExcelUtils.export2Web(response,fileName,"老人信息列表", LostmanExcel.class,excels);

    }


    /**
     * 发送短信接口
     */
    @ApiOperation(value = "发送短信")
    @PostMapping(value = "/sendMsg")
    public Result sendMsg(Long volunteerId){
        Volunteer volunteer = volunteerService.getById(volunteerId);
        if(volunteer == null){
            return Result.error().message("该志愿者不存在！");
        }
        Result result = SendSMSUtils.sendMsg(volunteerId, volunteer.getPhoneNumber(),null);
        return result;
    }


    /**
     * 分页查询走失老人列表
     */
    @ApiOperation("分页查询走失老人列表")
    @GetMapping("/findLostmanList")
    public Result findVolunteerList(FindLostmanReq findLostmanReq) {
        Page<LostmanInfo> page = new Page<>(findLostmanReq.getPageNum(), findLostmanReq.getPageSize());
        QueryWrapper<LostmanInfo> wrapper = getLostmanWrapper(findLostmanReq);
        IPage<LostmanInfo> lostmanPage = lostmanInfoService.findLostmanPage(page, wrapper);
        long total = lostmanPage.getTotal();
        List<LostmanInfo> lostmans = lostmanPage.getRecords();
        //这里拿到的records 全部字段都有 但是我们不用全部展示到前端 所以可以转成VO再放进去
        List<FamilyTaskDetailsVO> vos = new ArrayList<>();
        for (LostmanInfo lostman : lostmans) {
            Family family = familyService.getById(lostman.getFamilyId());
            List<LostmanImg> imgList = lostmanImgService.list(new QueryWrapper<LostmanImg>()
                    .eq("lostman_id", lostman.getLostmanId()));
            //转换成照片的vos
            List<ImgVO> imgVOS = ImgConverter.converterToImgVOList(imgList);
            //封装单个视图对象
            FamilyTaskDetailsVO vo = LostmanConverter.converterToVO(lostman, family);
            vo.setImgList(imgVOS);
            vos.add(vo);
        }
        return Result.success().data("total", total).data("records",vos);
    }


    /**
     * 封装分页的查询条件
     */
    private QueryWrapper<LostmanInfo> getLostmanWrapper(FindLostmanReq findLostmanReq) {
        QueryWrapper<LostmanInfo> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(findLostmanReq.getAddress())){
            queryWrapper.like("lostman_pos", findLostmanReq.getAddress());
        }
        if (!StringUtils.isEmpty(findLostmanReq.getLostmanName())) {
            queryWrapper.like("lostman_name", findLostmanReq.getLostmanName());
        }
        if (!StringUtils.isEmpty(findLostmanReq.getStatus()) && findLostmanReq.getStatus() != 2) {
            queryWrapper.eq("status", findLostmanReq.getStatus());
        }
        return queryWrapper;

    }


    /**
     * 分页查询管理员列表
     */
    @ApiOperation("分页查询管理员列表")
    @GetMapping("/getUserList")
    public Result getUserList(GetUserListReq getUserListReq) {
        Page<User> page = new Page<>(getUserListReq.getPageNum(), getUserListReq.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(getUserListReq.getVolunteerId())){
            wrapper.eq("id", getUserListReq.getVolunteerId());
        }
        IPage<User> userPage = userService.getUserPage(page, wrapper);
        long total = userPage.getTotal();
        List<User> users = userPage.getRecords();
        //转成页面需要的vo
        List<UserListVO> vos = new ArrayList<>();
        for (User user : users) {
            UserListVO vo = new UserListVO();
            //查用户权限中间表
            List<Long> roleIds = userRoleService.getRoleIdsByUid(user.getId());
            for (Long roleId : roleIds) {
                Role role = roleService.getById(roleId);
                BeanUtils.copyProperties(role,vo);
            }
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vos.add(vo);
        }
        return Result.success().data("total", total).data("records",vos);
    }




}

