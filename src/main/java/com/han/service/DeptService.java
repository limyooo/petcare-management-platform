package com.han.service;

import com.han.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
//根据id删除部门的方法
    void deleteById(Integer id);
//添加部门的方法
    void add(Dept dept);

//根据id查询部门的方法
    Dept getById(Integer id);
//修改部门信息的方法
    void update(Dept dept);
}
