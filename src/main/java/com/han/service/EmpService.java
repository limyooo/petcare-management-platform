package com.han.service;

import com.han.pojo.Emp;
import com.han.pojo.EmpQueryParam;
import com.han.pojo.PageResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {

    PageResult<Emp> page(EmpQueryParam  empQueryParam );

    void add(Emp emp);

    void delete(List<Integer> ids);


    Emp getInfo(Integer id);

    void update(Emp emp);
}

