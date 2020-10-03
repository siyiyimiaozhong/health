package com.siyi.service;

import com.siyi.pojo.User;

public interface UserService {
    public User findByUsername(String username);
}
