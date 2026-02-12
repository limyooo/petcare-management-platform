package com.han.service;

import com.han.pojo.EmpLog;
import org.springframework.stereotype.Service;

@Service
public interface EmpLogService {

    public void insertLog(EmpLog empLog);

}
