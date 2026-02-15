package com.han.exception;

import com.han.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GloablExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("服务器发生异常：{}", e.getMessage());
        return Result.error("服务器发生异常");
    }
    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("服务器发生异常：{}", e.getMessage());
        String msg = e.getMessage();
        int i = msg.indexOf("Duplicate entry");
        String substring = msg.substring(i);
        String[] split = substring.split(" ");
        return Result.error(split[2]+"已存在");
    }
    }

