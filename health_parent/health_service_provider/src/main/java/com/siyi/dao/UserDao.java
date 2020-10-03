package com.siyi.dao;

import com.siyi.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}
