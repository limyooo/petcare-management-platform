package com.han.controller;

import com.han.pojo.Emp;
import com.han.pojo.EmpQueryParam;
import com.han.pojo.PageResult;
import com.han.pojo.Result;
import com.han.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/emps")
/*员工管理*/
public class EmpController {
@Autowired
EmpService empService;
@GetMapping
    public Result page(EmpQueryParam empQueryParam)
{
    log.info("分页查询:{}",empQueryParam);
    PageResult<Emp> pageResult =empService.page(empQueryParam);
    return Result.success(pageResult);
}
@PostMapping
public Result save(@RequestBody Emp emp){
    log.info("保存员工:{}",emp);
    empService.add(emp);
    return Result.success();
}
}
