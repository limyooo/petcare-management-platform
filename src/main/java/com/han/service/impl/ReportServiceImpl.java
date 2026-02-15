package com.han.service.impl;

import com.han.mapper.EmpMapper;
import com.han.pojo.JobOption;
import com.han.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    @Override
    public JobOption getEmpJobData() {
        //调用mapper接口，获取统计数据
        List<Map<String, Object>> list = empMapper.countEmpJobDate();
        //组装结果，并返回
        List<Object> pos = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> num = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return new JobOption(pos, num);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {

        return empMapper.countEmpGenderDate();
    }
}
