package com.siyi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.siyi.dao.PermissionDao;
import com.siyi.dao.RoleDao;
import com.siyi.dao.UserDao;
import com.siyi.pojo.Permission;
import com.siyi.pojo.Role;
import com.siyi.pojo.User;
import com.siyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    //根据用户名查询数据库获取用户信息和关联的角色信息，同时需要查询角色关联的权限信息
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if(user == null) return null;
        //根据用户Id查询对应的角色
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色Id查询关联的权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);//让角色关联权限
        }
        user.setRoles(roles);//让用户关联角色
        return user;
    }
}
