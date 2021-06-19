package com.lxw.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里oss上传的工具类
 * 因为阿里给的接口 参数定死了 感觉不太好 就自己写一个用着
 */

@Component
public class AliOssUtils extends JwtTokenUtils {

    private static String bucketName;
    private static OSS ossClient = null;
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;

    /**
     * 静态属性要这样注入配置信息
     */
    @Value("${alioss.endpoint}")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Value("${alioss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        AliOssUtils.accessKeyId = accessKeyId;
    }

    @Value("${alioss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        AliOssUtils.accessKeySecret = accessKeySecret;
    }

    @Value("${alioss.bucketName}")
    public void setBucketName(String bucketName) {
        AliOssUtils.bucketName = bucketName;
    }


    /**
     * 创建OSS实例
     */
    public static void createBucket() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if (ossClient.doesBucketExist(bucketName)) {
            throw new RuntimeException(bucketName + "在对象存储的Bucket列表中已经存在");
        }
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传老人照片，先做一步判断
     * 1.如果走失老人ID存在，说明是家属上传的照片  => 那我们就要创建对应的文件夹 存放不同ID老人的照片
     * 2.走失老人ID为0,说明是志愿者上传的，那我们统一扔到志愿者目录下留底就完事
     */
    public static String upload(MultipartFile file, Long lostmanId) {
        String uploadUrl = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
            InputStream inputStream = file.getInputStream();
            String original = file.getOriginalFilename();
            String fileType = original.substring(original.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
            String newName = fileName + fileType;
            //如果lostmanId == 0 说明是志愿者上传的
            fileName = (lostmanId == 0) ? ("volunteer/" + newName) : ("lostman/" + lostmanId + "/" + newName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
            objectMetadata.setContentType(getcontentType(fileType));
            ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);
            ossClient.shutdown();
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
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
    public void download(String fileName) throws IOException {
        String objectName = fileName;
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
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
    public static void listFile() {
        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
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
    public static void deleteFile(String fileName) {
        // <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = fileName;

        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 断OSS服务文件上传时文件的contentType
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }


}
