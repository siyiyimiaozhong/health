package com.siyi.controller;

import com.siyi.constant.MessageConstant;
import com.siyi.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("getUsername")
    public Result getUsername(){
        //当spring security完成认证后，会将用户信息保存到框架上下文对象中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user!=null) {
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}
