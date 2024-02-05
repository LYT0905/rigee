package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.BaseContext;
import com.common.R;
import com.dto.OrdersDto;
import com.pojo.OrderDetail;
import com.pojo.Orders;
import com.pojo.ShoppingCart;
import com.service.OrderDetailService;
import com.service.OrdersService;
import com.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mark
 * @date 2024/2/5
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 购物车支付
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("支付成功");
    }

    /**
     * 查看订单(补充)
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>(page, pageSize);

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", BaseContext.getId()).orderByDesc("order_time");
        ordersService.page(pageInfo, queryWrapper);

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> orderDtoList = records.stream().map((item) ->{//item其实就是分页查询出来的每个订单对象
            OrdersDto orderDto = new OrdersDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            //调用根据订单id条件查询订单明细数据的方法，把查询出来订单明细数据存入orderDetailList
            List<OrderDetail> orderDetailList = ordersService.getOrderDetailListByOrderId(orderId);

            BeanUtils.copyProperties(item,orderDto);//把订单对象的数据复制到orderDto中
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //将订单分页查询的订单数据以外的内容复制到pageDto中，不清楚可以对着图看
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        dtoPage.setRecords(orderDtoList);
        return R.success(dtoPage);
    }

    /**
     * 再来一单(补充)
     * @param map
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Map<String, String> map){
        String ids = map.get("id");

        long id = Long.parseLong(ids);

        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);

        List<OrderDetail> list = orderDetailService.list(queryWrapper);

        // 获取·当前用户id
        Long userId = BaseContext.getId();
        // 查询当前用户的购物车数据
        QueryWrapper<ShoppingCart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        shoppingCartService.remove(queryWrapper1);

        List<ShoppingCart> shoppingCartList = list.stream().map((item) -> {
            //把从order表中和order_details表中获取到的数据赋值给这个购物车对象
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setImage(item.getImage());
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            if (dishId != null) {
                //如果是菜品那就添加菜品的查询条件
                shoppingCart.setDishId(dishId);
            } else {
                //添加到购物车的是套餐
                shoppingCart.setSetmealId(setmealId);
            }
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        //把携带数据的购物车批量插入购物车表  这个批量保存的方法要使用熟练！！！
        shoppingCartService.saveBatch(shoppingCartList);

        return R.success("操作成功");
    }

    /**
     * 后台显示订单详情
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(int page, int pageSize, Long number, String beginTime, String endTime){
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(number != null, "number", number).
                gt(StringUtils.hasLength(beginTime), "order_time", beginTime).
                lt(StringUtils.hasLength(endTime), "order_time", endTime);
        Page<Orders> page1 = ordersService.page(pageInfo, queryWrapper);
        return R.success(page1);
    }
}
