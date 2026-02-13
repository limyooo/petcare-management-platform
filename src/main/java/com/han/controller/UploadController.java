package com.han.controller;

import com.han.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile  file) throws IOException {
        log.info("接受参数:{},{},{}",name,age,file);

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.indexOf("."));
        String newFileName = UUID.randomUUID().toString()+substring;
        //保存文件
        file.transferTo(new File("/Users/hanlinyao/Desktop/web学习/2025最新版JavaWeb+AI"+newFileName));
        return Result.success();
        /*//获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
       String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新的文件名
        String newFileName = UUID.randomUUID().toString()+extension;
        //保存文件
        file.transferTo(new File("/Users/hanlinyao/Desktop/web学习/2025最新版JavaWeb+AI"+newFileName));
        return Result.success();*/
    }
}
