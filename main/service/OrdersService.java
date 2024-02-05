package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pojo.OrderDetail;
import com.pojo.Orders;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/5
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);

    List<OrderDetail> getOrderDetailListByOrderId(Long orderId);
}
