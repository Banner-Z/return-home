package com.lxw.system.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.lxw.entity.OssEntity;
import com.lxw.system.service.AliOssService;
import com.lxw.utils.AliOssUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 图片服务(先留着 感觉自己的工具类更灵活）
 */
@Service
public class AliOssServiceImpl implements AliOssService, InitializingBean {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    @Autowired
    private OssEntity ossEntity;

    /**
     * 初始化bean之后进行的操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        endpoint = ossEntity.getEndpoint();
        accessKeyId = ossEntity.getAccessKeyId();
        accessKeySecret = ossEntity.getAccessKeySecret();
        bucketName = ossEntity.getBucketName();
    }

    /**
     * 创建OSS实例
     */
    @Override
    public void createBucket() {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if (ossClient.doesBucketExist(bucketName)) {
            throw new RuntimeException(bucketName + "在对象存储的Bucket列表中已经存在");
        }
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     * 志愿者上传老人照片
     * @param file 文件对象
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        //上传地址
        String uploadUrl = null;
        try {
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
            //获取上传文件流
            InputStream inputStream = file.getInputStream();
            //获取上传文件的全名称
            String original = file.getOriginalFilename();
            System.out.println(original);
            //截取掉文件获得扩展名
            String fileType = original.substring(original.lastIndexOf("."));
            //去掉uuid中生成的-(比如 a980594d3-4545-d310-9345)
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
            //uuid + .jpg
            String newName = fileName + fileType;
//            //构建日期路径
//            String datePath = new DateTime().toString("yyyy-MM-dd");
            // yyyy-MM-dd+ /uuid +.jpg
            fileName = "volunteer/" + newName;
            //如果想要实现图片的预览效果,一定要设置一下几个点
            //1.设置文件 ACL为反正不能为私有  要么是公共读,要么是公共读写
            //2.一定要设置文本类型为(image/jpg)
            ObjectMetadata objectMetadata = new ObjectMetadata();
            //设置公共读权限
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
            //设置类型
            objectMetadata.setContentType(AliOssUtils.getcontentType(fileType));
            //文件上传至阿里云
            ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);
            // 关闭OSSClient。
            ossClient.shutdown();
            //默认十年不过期
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            //bucket名称  文件名   过期时间
            uploadUrl = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
            uploadUrl = uploadUrl.substring(0, uploadUrl.indexOf("?"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadUrl;
    }


    /**
     * 下载图片
     * @param fileName
     * @throws IOException
     */
    @Override
    public void download(String fileName) throws IOException {
        // <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
        InputStream content = ossObject.getObjectContent();
        if (content != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println("\n" + line);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            content.close();
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     * 列举文件
     */
    @Override
    public void listFile() {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        // objectListing.getObjectSummaries获取所有文件的描述信息。
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     * 删除图片
     * @param fileName
     */
    @Override
    public void deleteFile(String fileName) {
        // <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = fileName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }





}
