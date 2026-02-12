package com.han.controller;

import com.han.pojo.Dept;
import com.han.pojo.Result;
import com.han.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.AstFalse;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/depts")

public class DeptController {
    //固定的
    //private static final Logger log = LoggerFactory.getLogger(DeptController.class);
    @Autowired
    private DeptService deptService;
    /*
    查询所有部门数据
    * */
    @GetMapping
    public Result list() {
        //System.out.println("Query all departmental data");
        log.info("Query all departmental data");
        List<Dept> deptList =deptService.findAll();
        return Result.success(deptList);
    }
    /*删除*/
    //requestParam,该参数在请求时必须传递，要不然就会报错，默认required为true
    @DeleteMapping
    public Result delete(Integer id){
        //System.out.println("根据id删除部门: {}", id);
        log.info("根据id删除部门"+ id);
        deptService.deleteById(id);
        return Result.success();//不需要返回数据，所以直接用无参的

    }
    //添加,请求参数是在请求体中传递过来的是json格式的请求参数
    @PostMapping
    public Result add(@RequestBody Dept dept){
        //System.out.println("添加部门"+ dept);
        log.info("添加部门"+ dept);
        deptService.add(dept);
        return Result.success();
    }
    //根据id查询部门
    @GetMapping("/{id}") //路径参数
    public Result getInfo(@PathVariable Integer id){
        //System.out.println("根据id查询部门: {}"id);
        log.info("根据id查询部门: {}"+ id);
        Dept dept =deptService.getById(id);//有返回值，要返回给前端而且只会返回一条信息
        return Result.success(dept);
    }
    //修改部门
    @PutMapping
    public Result update(@RequestBody Dept dept){
        //System.out.println("修改部门"+ dept);
        log.info("修改部门"+ dept);
        deptService.update(dept);
        return Result.success();
    }

}
