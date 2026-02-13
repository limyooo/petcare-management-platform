package com.han.controller;

import com.han.pojo.Result;
import com.han.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.jacoco.agent.rt.internal_43f5073.core.runtime.AgentOptions.OutputMode.file;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    @PostMapping("/upload")
        public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传:{}",file.getOriginalFilename() );
        //oss
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("文件上传成功:{}",url);
        return Result.success(url);
    }

    private Result m1(String name, Integer age, MultipartFile file) throws IOException {
        log.info("接受参数:{},{},{}", name, age, file);

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.indexOf("."));
        String newFileName = UUID.randomUUID().toString()+substring;
        //保存文件
        file.transferTo(new File("/Users/hanlinyao/Desktop/web学习/2025最新版JavaWeb+AI"+newFileName));
        return Result.success();
    }
}
