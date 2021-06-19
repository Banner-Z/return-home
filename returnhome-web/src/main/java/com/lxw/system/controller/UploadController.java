package com.lxw.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxw.dto.BodyDetResDTO;
import com.lxw.entity.LostmanImg;
import com.lxw.entity.LostmanInfo;
import com.lxw.handler.BusinessException;
import com.lxw.response.Result;
import com.lxw.response.ResultCode;
import com.lxw.system.service.AliOssService;
import com.lxw.system.service.LostmanImgService;
import com.lxw.system.service.LostmanInfoService;
import com.lxw.system.service.impl.AliOssServiceImpl;
import com.lxw.utils.AliOssUtils;
import com.lxw.utils.FaceDetection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "上传照片模块")
@RestController
@RequestMapping("/upload")
@Transactional
public class UploadController {

    @Autowired
    private LostmanImgService lostmanImgService;

    @Autowired
    private LostmanInfoService lostmanInfoService;


    /**
     * 对传入的参数进行预处理
     * 1.判断该ID老人是否存在
     * 2.判断传入的文件二进制流是否为空
     */
    public void judgeParams(MultipartFile file,Long lostmanId){
        LostmanInfo lostman = lostmanInfoService.getById(lostmanId);
        if(lostman == null) {
            throw new BusinessException(ResultCode.LOSTMAN_NOT_EXIST.getCode(),ResultCode.LOSTMAN_NOT_EXIST.getMessage());
        }
        if(file.getSize() == 0){
            throw new BusinessException(ResultCode.ERROR_FILE_FORMAT.getCode(),ResultCode.ERROR_FILE_FORMAT.getMessage());
        }
    }



    /**
     * 家属上传老人脸部照片
     * 1.上传到阿里OSS
     * 2.上传到人脸识别的库
     * 3.插入数据库
     */
    @ApiOperation("家属上传老人脸部照片")
    @PostMapping("/family/face")
    public Result familyUpload(@RequestParam("file") MultipartFile file,
                               @RequestParam("lostmanId") Long lostmanId) throws IOException, InterruptedException {
        //参数预处理
        judgeParams(file,lostmanId);
        //上传到阿里OSS 取得图片Url
        String uploadUrl = AliOssUtils.upload(file,lostmanId);
        //得判断这个人脸照片的年龄是否超过预期
        judgeLostmanAge(uploadUrl);

        //上传到人脸识别的库
        setWarehouse(uploadUrl,lostmanId);

        //拿到图片的url后  还要对应插入到老人和图片的数据库中
        LostmanImg lostmanImg = new LostmanImg();
        lostmanImg.setLostmanId(lostmanId);
        lostmanImg.setPicUrl(uploadUrl);
        lostmanImgService.save(lostmanImg);
        return Result.success().message("上传老人照片成功").data("url",uploadUrl);
    }


    /**
     * 判断上传照片的人脸年龄是否超过60（满足报案需求）
     */
    public void judgeLostmanAge(String uploadUrl) throws IOException, InterruptedException {
        int ageDet = FaceDetection.faceAgeDet(uploadUrl, "detect", "0");
        System.out.println("该老人照片的年龄是: "+ageDet);
        //如果照片年龄没超过60 那就没达到报案要求 就取消报案
        if(ageDet < 60){
            BusinessException exception = new BusinessException(ResultCode.LOSTMAN_AGE_NOT_RIGHT.getCode(),
                    ResultCode.LOSTMAN_AGE_NOT_RIGHT.getMessage());
            throw exception;
        }

    }



    /**
     * 上传老人照片， 建立/更新人脸库
     * 我们要确定是新建用户还是更新用户,
     * 所以得判断该老人在数据库中是否有过照片
     */
    public void setWarehouse(String uploadUrl,Long lostmanId) throws IOException, InterruptedException {
        QueryWrapper<LostmanImg> queryWrapper = new QueryWrapper();
        queryWrapper.eq("lostman_id",lostmanId).eq("type",0);
        List<LostmanImg> imgList = lostmanImgService.list(queryWrapper);
        //如果没有找到 说明此老人是第一次上传照片
        if (imgList.size() == 0){
            System.out.println("这是第一次上传照片");
            FaceDetection.faceDet(uploadUrl,"enrollUser","lostman"+lostmanId);
        }else{
            System.out.println("更新人脸库中的照片");
            FaceDetection.faceDet(uploadUrl,"updateFace","lostman"+lostmanId);
        }

    }



    /**
     * 家属上传老人 体态照片
     * 1.上传到阿里OSS
     * 2.上传到人脸识别的库
     * 3.插入数据库
     */
    @ApiOperation("家属上传老人体态照片")
    @PostMapping("/family/body")
    public Result familyUploadBody(@RequestParam("file") MultipartFile file,
                                   @RequestParam("lostmanId") Long lostmanId) throws IOException, InterruptedException {
        //参数预处理
        judgeParams(file,lostmanId);
        //老人存在，就上传到阿里OSS 取得图片Url
        String uploadUrl = AliOssUtils.upload(file,lostmanId);
        //上传到人脸识别的库
        setBodyWarehouse(uploadUrl,lostmanId);
        //拿到图片的url后  还要对应插入到老人和图片的数据库中
        LostmanImg lostmanImg = new LostmanImg();
        lostmanImg.setLostmanId(lostmanId);
        lostmanImg.setPicUrl(uploadUrl);
        lostmanImg.setType(1);  //身体照片
        lostmanImgService.save(lostmanImg);
        return Result.success().message("上传老人照片成功").data("url",uploadUrl);
    }



    /**
     * 上传老人体态照片， 建立/更新人体态库
     * 我们要确定是新建用户还是更新用户,
     * 所以得判断该老人在数据库中是否有过照片
     */
    public void setBodyWarehouse(String uploadUrl,Long lostmanId) throws IOException, InterruptedException {
        QueryWrapper<LostmanImg> queryWrapper = new QueryWrapper();
        queryWrapper.eq("lostman_id",lostmanId).eq("type",1);
        List<LostmanImg> imgList = lostmanImgService.list(queryWrapper);
        //如果没有找到 说明此老人是第一次上传照片
        if (imgList.size() == 0){
            System.out.println("这是第一次上传照片");
            FaceDetection.bodyDet(uploadUrl,"enrollUser","lostman"+lostmanId);
        }else{
            System.out.println("更新人脸库中的照片");
            FaceDetection.bodyDet(uploadUrl,"updateBody","lostman"+lostmanId);
        }

    }





    /**
     * 下方是志愿者的接口
     */

    /**
     * 志愿者上传老人照片进行人脸识别
     * 传参进来的lostmanId 不是上传OSS时用的！！！
     */
    @ApiOperation("志愿者上传照片,进行人脸识别")
    @PostMapping("/volunteer/face")
    public Result faceDetection(@RequestParam("file") MultipartFile file,
                                @RequestParam("lostmanId") Long lostmanId) throws IOException, InterruptedException {
        //参数预处理
        judgeParams(file,lostmanId);
        //这里一定要传0 表示是志愿者上传的照片
        String uploadUrl = AliOssUtils.upload(file,Long.valueOf(0));
        //对比照片,获取相似度
        Double similarity = FaceDetection.faceDet(uploadUrl,"search","lostman"+lostmanId);
        return Result.success().data("url",uploadUrl).data("similarity",similarity);
    }


    /**
     * 志愿者上传老人照片  进行体态识别
     * 传参进来的lostmanId 不是上传OSS时用的！！！
     */
    @ApiOperation("志愿者上传老人照片,进行体态识别")
    @PostMapping("/volunteer/body")
    public Result bodyDetection(@RequestParam("file") MultipartFile file,
                                @RequestParam("lostmanId") Long lostmanId) throws IOException, InterruptedException {
        //参数预处理
        judgeParams(file,lostmanId);
        //这里一定要传0 表示是志愿者上传的照片
        String uploadUrl = AliOssUtils.upload(file,Long.valueOf(0));
        //对比照片,获取相似度
        BodyDetResDTO dto = FaceDetection.bodyDet(uploadUrl,"search","lostman"+lostmanId);
        return Result.success().data("url",uploadUrl).data("similarity",dto.getScore());

    }







}
