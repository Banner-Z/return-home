package com.lxw.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxw.dto.BodyDetResDTO;
import com.lxw.handler.BusinessException;
import com.lxw.response.ResultCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 人脸识别工具类
 */
public class FaceDetection {

    // 一些配置常量
    private static final String group_id = "g1";
    private static final String body_group = "bodyG1";

    //本地路径
//    private static final String executer = "python";
//    private static final String file_path = "E:\\return-home\\returnhome-common\\src\\main\\resources\\face.py";
//    private static final String file_path2 = "E:\\return-home\\returnhome-common\\src\\main\\resources\\body.py";


//    //服务器配置
    private static final String executer = "python3";
    private static final String file_path = "/www/wwwroot/www.lanotherl.com/face.py";  //python路径
    private static final String file_path2 = "/www/wwwroot/www.lanotherl.com/body.py";  //python路径


    //动态参数
//    private static final String user_id = "dawda";
//    private static final String func = "detect";//1.search 2.detect
//    private static final String image_name = "http://return-home.oss-cn-hangzhou.aliyuncs.com/volunteer/11cc08f5483b4d1b99d8ce315e4dd104.jpg";
//
//    enrollGroup、enrollUser、updateBody、deleteGroup、deleteUser
//    分别是创建组、用户（同时传一张照片）、添加人体图片、删除组、用户
//            添加人脸图片是updateFace


    /**
     * 人脸年龄判断（不符合就报案失败)
     * @param image_name 照片url
     * @param func 函数名
     * @param user_id  用户名
     */
    public static int faceAgeDet(String image_name, String func, String user_id) throws InterruptedException, IOException {
        String[] command_line = new String[]{executer, file_path, func, image_name, group_id, user_id};
        Process process = Runtime.getRuntime().exec(command_line);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        JSONObject jsonObject = null;
        while ((line = in.readLine()) != null) {
            line = line.replaceAll("'","\"");
            line = line.replaceAll("None","null");
            System.out.println(line);
            //处理json字符串
            jsonObject = JSON.parseObject(line);
        }
        in.close();
        // java代码中的 process.waitFor() 返回值（和我们通常意义上见到的0与1定义正好相反）
        // 返回值为0 - 表示调用python脚本成功；
        // 返回值为1 - 表示调用python脚本失败。
        int res = process.waitFor();
        System.out.println("调用 python 脚本: " + (res == 0 ? "成功" : "失败"));
        return handleAgeRes(jsonObject);
    }


    /**
     * 处理人脸年龄判断结果
     * 没成功就抛出异常
     */
    public static int handleAgeRes(JSONObject jsonObject){
        int code = jsonObject.getIntValue("error_code");
        String msg = jsonObject.getString("error_msg");
        //如果没成功就抛出自定义的业务异常
        if(code != 0){
            BusinessException exception = new BusinessException(code,msg);
            throw exception;
        }
        if(!jsonObject.getJSONObject("result").isEmpty()){
            int age = jsonObject.getJSONObject("result").getJSONArray("face_list")
                    .getJSONObject(0).getInteger("age");
            return age;
        }
        return 0;
    }



    /**
     * 人脸识别的方法
     * @param image_name 照片url
     * @param func 函数名
     * @param user_id  用户名
     */
    public static Double faceDet(String image_name, String func, String user_id) throws InterruptedException, IOException {
        String[] command_line = new String[]{executer, file_path, func, image_name, group_id, user_id};
        Process process = Runtime.getRuntime().exec(command_line);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        JSONObject jsonObject = null;
        while ((line = in.readLine()) != null) {
            line = line.replaceAll("'","\"");
            line = line.replaceAll("None","null");
            System.out.println(line);
            //处理json字符串
            jsonObject = JSON.parseObject(line);
        }
        in.close();
        // java代码中的 process.waitFor() 返回值（和我们通常意义上见到的0与1定义正好相反）
        // 返回值为0 - 表示调用python脚本成功；
        // 返回值为1 - 表示调用python脚本失败。
        int res = process.waitFor();
        System.out.println("调用 python 脚本: " + (res == 0 ? "成功" : "失败"));
        //对结果进行处理
        Double similarity = handleFaceRes(jsonObject,func);
        return similarity;

    }



    /**
     * 处理人脸识别结果
     * 没成功就抛出异常
     */
    public static Double handleFaceRes(JSONObject jsonObject,String func){
        int code = jsonObject.getIntValue("error_code");
        String msg = jsonObject.getString("error_msg");
        //如果没成功就抛出自定义的业务异常
        if(code != 0){
            BusinessException exception = new BusinessException(code,msg);
            throw exception;
        }
        //如果进行人脸识别
        System.out.println(func.equals("search"));
        if(func.equals("search")){
            if(!jsonObject.getJSONObject("result").isEmpty()){
                Double similarity = jsonObject.getJSONObject("result").getJSONArray("face_list")
                    .getJSONObject(0).getJSONArray("user_list")
                    .getJSONObject(0).getDouble("score");
            return similarity;
            }
        }
        return 0.0;
    }



    /**
     * 体态识别
     */
    public static BodyDetResDTO bodyDet(String image_name, String func, String lostmanId) throws InterruptedException, IOException {
        String[] command_line = new String[]{executer, file_path2, func, image_name, body_group, lostmanId};
        Process process = Runtime.getRuntime().exec(command_line);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        JSONObject jsonObject = null;
        while ((line = in.readLine()) != null) {
            line = line.replaceAll("'","\"");
            line = line.replaceAll("None","null");
            System.out.println(line);
            //处理json字符串
            jsonObject = JSON.parseObject(line);
        }
        in.close();
        // java代码中的 process.waitFor() 返回值（和我们通常意义上见到的0与1定义正好相反）
        // 返回值为0 - 表示调用python脚本成功；
        // 返回值为1 - 表示调用python脚本失败。
        int res = process.waitFor();
        System.out.println("调用 python 脚本: " + (res == 0 ? "成功" : "失败"));
        //对结果进行处理
        if(func.equals("enrollUser")||func.equals("updateBody")){
            return null;
        }
        BodyDetResDTO dto = handleBodyRes(jsonObject,lostmanId);
        return dto;
    }


    /**
     * 处理体态识别结果
     * 没成功就抛出异常
     */
    public static BodyDetResDTO handleBodyRes(JSONObject jsonObject,String lostmanId) {
        JSONArray candidates = jsonObject.getJSONArray("Candidates");
        if (candidates == null) {
            BusinessException exception = new BusinessException(ResultCode.LOSTMAN_PIC_NOT_EXIST.getCode(),
                    ResultCode.LOSTMAN_PIC_NOT_EXIST.getMessage());
            throw exception;
        }
        System.out.println(candidates);
//        List<BodyDetResDTO> dtos = new ArrayList<>();
        BodyDetResDTO dto = null;
        for (Object candidate : candidates) {
            dto = JSONObject.toJavaObject((JSON) candidate, BodyDetResDTO.class);
            //如果是同一个老人我们就退出循环
            if (dto.getPersonId().equals(lostmanId)) {
                break;
            }
        }
        return dto;
    }




}



