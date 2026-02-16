package com.han.service;

import com.han.pojo.Owner;
import com.han.pojo.OwnerQueryParam;
import com.han.pojo.PageResult;

import java.util.List;

public interface OwnerService {
    PageResult<Owner> page(OwnerQueryParam param);

    void add(Owner owner);

    void delete(List<Integer> ids);

    Owner get(Integer id);

    void update(Owner owner);
}
