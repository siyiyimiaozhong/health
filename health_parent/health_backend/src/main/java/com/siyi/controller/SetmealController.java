package com.siyi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.siyi.constant.MessageConstant;
import com.siyi.constant.RedisConstant;
import com.siyi.entity.PageResult;
import com.siyi.entity.QueryPageBean;
import com.siyi.entity.Result;
import com.siyi.pojo.Setmeal;
import com.siyi.service.SetmealService;
import com.siyi.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("setmeal")
public class SetmealController {

    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

    @RequestMapping("upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imFile){
        System.out.println(imFile.getOriginalFilename());
        String originalFilename = imFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf('.');
        String extention = originalFilename.substring(index);//.jpg
        String fileName = UUID.randomUUID().toString()+extention;
        try {
            QiniuUtils.upload2Qiniu(imFile.getBytes(),fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @RequestMapping("add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try{
            setmealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.pageQuery(queryPageBean);
    }
}
