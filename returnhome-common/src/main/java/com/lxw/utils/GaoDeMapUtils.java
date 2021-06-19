package com.lxw.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxw.handler.BusinessException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 高德地图api 进行坐标转换 距离计算
 */
public class GaoDeMapUtils {

    private final static String key ="db8b84e72c2370d87a76a6998320f67d";


    /**
     * 根据地址查询经纬度
     */
    public static JSONObject getLngAndLat(String address) {
        JSONObject positionObj = new JSONObject();
        // 拼接请求高德的url
        String url = "http://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=JSON&key=" + key;
        // 请求高德接口
        String result = sendHttpGet(url);
        JSONObject resultJOSN = JSONObject.parseObject(result);
        System.out.println("高德接口返回原始数据：");
        System.out.println(resultJOSN);
        JSONArray geocodesArray = resultJOSN.getJSONArray("geocodes");
        if(geocodesArray.size() == 0){
            BusinessException exception = new BusinessException(3001,"输入的地址有误");
            throw exception;
        }
        if (geocodesArray.size() > 0) {
            String position = geocodesArray.getJSONObject(0).getString("location");
            String[] lngAndLat = position.split(",");
            String longitude = lngAndLat[0];
            String latitude = lngAndLat[1];
            positionObj.put("longitude", longitude);
            positionObj.put("latitude", latitude);
        }
        geocodesArray.getJSONObject(0).getString("location");
        return positionObj;
    }

    /**
     * 发送Get请求
     *
     * @param url
     * @return
     */
    public static String sendHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(10000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        String result = "";
        try {
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
            HttpEntity entity = closeableHttpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}

