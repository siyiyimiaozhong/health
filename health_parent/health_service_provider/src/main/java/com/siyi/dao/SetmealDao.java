package com.siyi.dao;

import com.github.pagehelper.Page;
import com.siyi.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map map);

    public Page<Setmeal> findByCondition(String queryString);

    public List<Setmeal> findAll();

    public Setmeal findById4Detail(int id);

    public Setmeal findById(int id);

    public List<Map<String, Object>> findSetmealCount();
}
