package com.han.mapper;

import com.han.pojo.Emp;
import com.han.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/*员工信息*/
@Mapper
public interface EmpMapper {
/*  @Select("select count(*) from emp e left join pet_department d on e.dept_id = d.id ")
    public Long count();// 总数量
    @Select("select e.*,d.name deptName from emp e left join pet_department d on e.dept_id = d.id order by e.update_time desc limit #{index},#{pageSize};")
    public List<Emp> list(Integer index, Integer pageSize);//展示的*/
    //pagehelper
    //@Select("select e.*,d.name deptName from emp e left join pet_department d on e.dept_id = d.id order by e.update_time desc")
     public List<Emp> list(EmpQueryParam empQueryParam);//展示的

@Options(useGeneratedKeys = true, keyProperty = "id")// 设置主键回填
@Insert("insert into emp(username,password,name,gender,phone,job,salary,image,entry_date,dept_id) values(#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId})")
    void insert(Emp emp);

    void deleteByIds(List<Integer> ids);

    Emp getInfo(Integer id);

    void updateById(Emp emp);
    //作为key
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobDate();

    @MapKey("name")
    List<Map<String, Object>> countEmpGenderDate();

    Emp selectByUsernameAndPassword(Emp emp);
}
