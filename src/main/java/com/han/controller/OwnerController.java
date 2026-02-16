package com.han.controller;

import com.han.pojo.Owner;
import com.han.pojo.OwnerQueryParam;
import com.han.pojo.PageResult;
import com.han.pojo.Result;
import com.han.service.OwnerService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/owners")
public class OwnerController {
    @Autowired
   private OwnerService ownerService;
    /*分页查询*/
    @GetMapping
   public Result page(OwnerQueryParam param){
        log.info("分页查询参数：{}",param);
        PageResult<Owner> pageResult = ownerService.page(param);
        return Result.success(pageResult);
    }
    /*增加*/
    @PostMapping
    public Result save(@RequestBody Owner owner){
        log.info("保存员工:{}",owner);
        ownerService.add(owner);
        return Result.success();
    }
    /*删除，@RequestParam是吧前端返回的url里面的id提取出来放在一个list里面*/
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工：{}", ids);
        ownerService.delete(ids);
        return Result.success();
    }
    /*查，@PathVariable取的是id的匹配*/
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id){
        log.info("查询员工：{}", id);
        Owner owner = ownerService.get(id);
        return Result.success(owner);
    }
    /*改*/
    @PutMapping
    public Result update(@RequestBody Owner owner){
        log.info("更新员工：{}", owner);
        ownerService.update(owner);
        return Result.success();
    }
}
