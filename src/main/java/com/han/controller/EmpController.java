package com.han.controller;

import com.han.pojo.Emp;
import com.han.pojo.EmpQueryParam;
import com.han.pojo.PageResult;
import com.han.pojo.Result;
import com.han.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
@DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
    log.info("删除员工：{}", ids);
    empService.delete(ids);
    return Result.success();
    }
    @GetMapping("/{id}")
    public Result getIno(@PathVariable Integer id){
    log.info("查询员工信息:{}", id);
    Emp emp = empService.getInfo(id);
    return Result.success(emp);
    }
    @PutMapping
    public Result update(@RequestBody Emp emp){
    log.info("修改员工信息:{}", emp);
    empService.update(emp);
    return Result.success();
    }

}
