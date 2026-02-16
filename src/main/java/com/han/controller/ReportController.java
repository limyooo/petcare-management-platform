package com.han.controller;

import com.han.pojo.JobOption;
import com.han.pojo.Result;
import com.han.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("获取员工工作数据");
       JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("获取员工性别数据");
         List<Map<String, Object>> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }
    @GetMapping("/ownerGenderData")
    public Result getOwnerGenderData(){
        log.info("获取customer性别数据");
        List<Map<String, Object>> genderList = reportService.getOwnerGenderData();
        return Result.success(genderList);
    }
    @GetMapping("/ownerAddressData")
    public Result getOwnerAddressData(){
        log.info("获取customer地址数据");
        List<Map<String, Object>> addressList = reportService.getOwnerAddressData();
        return Result.success(addressList);
    }
}
