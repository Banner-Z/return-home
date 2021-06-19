package com.lxw.utils;

import com.lxw.response.Result;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;;

public class SendSMSUtils {

    public static Result sendMsg(Long volunteerId,String phoneNumber,Long taskId) {
        try{
            Credential cred = new Credential("AKIDucomX3GLEBtviHFhjX8TadLyNBk4czmL", "VkjU2KNGyTxG1h7HA9Bz2wUVM2efmh6v");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);
            SendSmsRequest req = new SendSmsRequest();

            String[] phoneNumberSet1 = {"+86"+phoneNumber};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setTemplateID("911107");
            req.setSign("lanotherl网");

            String[] templateParamSet1;
            if(taskId == null){
                templateParamSet1 = new String[]{"志愿者 " + volunteerId + "号"};
            }else{
                templateParamSet1 = new String[]{"任务编号: " + taskId};
            }
            req.setTemplateParamSet(templateParamSet1);

            req.setSmsSdkAppid("1400496513");

            SendSmsResponse resp = client.SendSms(req);
            System.out.println(SendSmsResponse.toJsonString(resp));
            if(resp.getSendStatusSet()[0].getCode().equals("Ok")){
                return Result.success().message("发送短信成功！");
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return Result.error().message("发送短信失败");
        }
        return Result.error().message("发送短信失败！");
    }

}