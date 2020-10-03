package com.siyi.service;

import com.siyi.entity.PageResult;
import com.siyi.entity.QueryPageBean;
import com.siyi.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    List<CheckItem> finAll();
}
