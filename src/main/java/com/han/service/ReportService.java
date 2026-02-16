package com.han.service;

import com.han.pojo.JobOption;

import java.util.List;
import java.util.Map;


public interface ReportService {
    JobOption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    List<Map<String, Object>> getOwnerGenderData();

    List<Map<String, Object>> getOwnerAddressData();
}
