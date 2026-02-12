package com.han.mapper;

import com.han.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

//quary all depts data
    @Select("select id, name, description, create_time , update_time from petcare.pet_department order by update_time desc ")
    List<Dept> findAll();

//根据id删除部门
    @Delete("delete from petcare.pet_department where id=#{?}")
    void deleteById(Integer id);
    //添加部门
    @Insert("insert into petcare.pet_department(name,create_time,update_time) values (#{name},#{createTime},#{updateTime})")
    void inset(Dept dept);

    //根据id查询部门
@Select("select id, name, description, create_time , update_time from petcare.pet_department where id=#{?}")
    Dept getById(Integer id);

//修改部门
@Update("update petcare.pet_department set name=#{name},update_time=#{updateTime} where id=#{id}")
    void update(Dept dept);
}
