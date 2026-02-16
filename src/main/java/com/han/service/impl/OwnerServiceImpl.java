package com.han.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.han.mapper.OwnerMapper;
import com.han.pojo.Owner;
import com.han.pojo.OwnerQueryParam;
import com.han.pojo.PageResult;
import com.han.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerMapper ownerMapper;
    @Override
    public PageResult<Owner> page(OwnerQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());
        List<Owner> ownerList = ownerMapper.list(param);
        Page<Owner> p = (Page<Owner>) ownerList;
        return new PageResult<>(p.getTotal(), p.getResult());

    }

    @Override
    public void add(Owner owner) {
        /*修改时间*/
        owner.setCreateTime(LocalDateTime.now());
        owner.setUpdateTime(LocalDateTime.now());
        ownerMapper.insert(owner);

    }

    @Override
    public void delete(List<Integer> ids) {
        ownerMapper.deleteByOwnerIds(ids);

    }

    @Override
    public Owner get(Integer id) {
        return ownerMapper.get(id);

    }

    @Override
    public void update(Owner owner) {
        owner.setUpdateTime(LocalDateTime.now());
        ownerMapper.updateByOwnerId(owner);
    }
}
