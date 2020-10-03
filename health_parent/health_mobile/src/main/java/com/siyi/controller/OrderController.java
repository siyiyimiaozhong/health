package com.siyi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.siyi.constant.MessageConstant;
import com.siyi.constant.RedisMessageConstant;
import com.siyi.entity.Result;
import com.siyi.pojo.Order;
import com.siyi.service.OrderService;
import com.siyi.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约处理
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    //在线体检预约
    @RequestMapping("submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String)map.get("telephone");
        //从Redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String)map.get("validateCode");
        //将用户输入的验证码和Redis中保存的验证码进行比对
        if(validateCodeInRedis!=null && validateCode!=null && validateCode.equals(validateCodeInRedis)){
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型，分为微信预约，电话预约
            Result result = new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            try{
                result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                //预约成功，可以为用户发送短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,(String)map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
