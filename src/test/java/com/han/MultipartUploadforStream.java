package com.han;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.internal.Mimetypes;
import com.aliyun.oss.model.*;

public class MultipartUploadforStream {
    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其他Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Bucket名称，例如examplebucket。
        String bucketName = "java-petcare";

        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "001.jpg";

        // 待上传本地文件路径。
        String filePath = "/Users/hanlinyao/Desktop/截屏2026-02-06 15.13.00.png";

        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-beijing";

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

            ObjectMetadata metadata = new ObjectMetadata();
            if (metadata.getContentType() == null) {
                metadata.setContentType(Mimetypes.getInstance().getMimetype(new File(filePath), objectName));
            }
            System.out.println("Content-Type: " + metadata.getContentType());
            request.setObjectMetadata(metadata);

            // 初始化分片上传，返回uploadId。
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            String uploadId = upresult.getUploadId();

            // partETags是PartETag的集合。
            List<PartETag> partETags = new ArrayList<>();

            // 计算文件分片数量。
            File sampleFile = new File(filePath);
            long fileLength = sampleFile.length();
            final long partSize = 5 * 1024 * 1024L; // 每个分片大小为5MB。

            // 遍历分片并上传。
            try (FileInputStream instream = new FileInputStream(sampleFile)) {
                byte[] buffer = new byte[8192]; // 设置缓冲区
                long remaining = fileLength;
                int partNumber = 1;

                while (remaining > 0) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    long bytesToRead = Math.min(partSize, remaining);

                    int bytesRead;
                    long totalRead = 0;
                    while (totalRead < bytesToRead && (bytesRead = instream.read(buffer)) > 0) {
                        baos.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;
                    }

                    // 构造分片流
                    InputStream partStream = new ByteArrayInputStream(baos.toByteArray());

                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    uploadPartRequest.setBucketName(bucketName);
                    uploadPartRequest.setKey(objectName);
                    uploadPartRequest.setUploadId(uploadId);
                    uploadPartRequest.setInputStream(partStream);
                    uploadPartRequest.setPartSize(totalRead);
                    uploadPartRequest.setPartNumber(partNumber++);

                    UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                    partETags.add(uploadPartResult.getPartETag());

                    remaining -= totalRead;
                }
            }

            // 创建CompleteMultipartUploadRequest对象。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

            // 完成分片上传。
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            System.out.println("上传成功，ETag：" + completeMultipartUploadResult.getETag());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught a ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            // 关闭OSSClient。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}