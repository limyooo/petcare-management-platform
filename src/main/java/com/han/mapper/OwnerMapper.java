package com.han.mapper;

import com.han.pojo.Owner;
import com.han.pojo.OwnerQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OwnerMapper {

    List<Owner> list(OwnerQueryParam param);

    void insert(Owner owner);

    void deleteByOwnerIds(List<Integer> ids);

    Owner get(Integer id);

    void updateByOwnerId(Owner owner);

    List<Map<String, Object>> countByGender();

    List<Map<String, Object>> countByAddress();
}
