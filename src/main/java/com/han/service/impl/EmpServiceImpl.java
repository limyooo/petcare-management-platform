package com.han.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.han.mapper.EmpExprMapper;
import com.han.mapper.EmpMapper;
import com.han.pojo.*;
import com.han.service.EmpLogService;
import com.han.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
public class EmpServiceImpl implements EmpService {
@Autowired
private EmpMapper empMapper;
@Autowired
private EmpExprMapper empExprMapper;
@Autowired
private EmpLogService empLogService;
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        //设置分页参数
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        //执行查询
        List<Emp> empList = empMapper.list(empQueryParam);
        //解析查询结果并封装
       Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(), p.getResult());
    }
    @Transactional(rollbackFor = {Exception.class})//出现所有异常都回滚
    @Override
    public void add(Emp emp) {
        try {
            //保存员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            //工作经历
            List<EmpExpr> exprList = emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                //遍历集合为empid赋值
                for (EmpExpr empExpr : exprList) {
                    empExpr.setEmpId(emp.getId());
                }
                empExprMapper.insert(exprList);
            }
        } finally {
            //记录日志,我要保证这里面的代码不管上面成不成功都要运行
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "添加员工信息");
            empLogService.insertLog(empLog);
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(List<Integer> ids) {
        //删除员工基本信息
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
        //删除员工工作经历
    }

    @Override
    public Emp getInfo(Integer id) {
       return  empMapper.getInfo(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void update(Emp emp) {
        //根据id修改员工的基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
        //根据id修改员工的工作经历
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        //添加工作经历
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            //遍历集合为empid赋值
            for (EmpExpr empExpr : exprList) {
                empExpr.setEmpId(emp.getId());
            }
            empExprMapper.insert(exprList);
        }

        //先删除
        //在添加
    }


}

