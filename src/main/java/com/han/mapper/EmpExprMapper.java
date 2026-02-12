package com.han.mapper;

import com.han.pojo.EmpExpr;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*员工工作经历*/
@Mapper
public interface EmpExprMapper {

    void insert(List<EmpExpr> exprList);
}
