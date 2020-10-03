package com.siyi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.siyi.constant.MessageConstant;
import com.siyi.dao.MemberDao;
import com.siyi.dao.OrderDao;
import com.siyi.dao.OrderSettingDao;
import com.siyi.entity.Result;
import com.siyi.pojo.Member;
import com.siyi.pojo.Order;
import com.siyi.pojo.OrderSetting;
import com.siyi.service.OrderService;
import com.siyi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public Result order(Map map) throws Exception {
        //1. 检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String)map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2. 检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(reservations >= number){
            //已经约满，无法预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3. 检查用户是否重复预约（同一个用户在同一天预约了同一个套餐）
        String telephone = (String)map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
            Integer memberId = member.getId();
            Date order_Date = DateUtils.parseString2Date(orderDate);
            String setmealId = (String)map.get("setmealId");
            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderDate(order_Date);
            order.setSetmealId(Integer.parseInt(setmealId));
            //根据条件进行查询
            List<Order> orders = orderDao.findByCondition(order);
            if(orders !=null && orders.size()>0){
                //说明用户在重复预约，无法完成再次预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else{
            //4. 检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String)map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动填充会员
        }
        //5. 预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String)map.get("orderType"));
        order.setOrderStatus(Order.ORDERTYPE_WEIXIN);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editNumberByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约Id查询预约相关信息（体检人姓名，预约日期，套餐名称，预约类型）
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map!=null){
            //处理日期格式
            Date orderDate = (Date)map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
