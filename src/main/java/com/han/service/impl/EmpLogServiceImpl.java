package com.han.service.impl;

import com.han.mapper.EmpLogMapper;
import com.han.pojo.EmpLog;
import com.han.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
}
