package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.OrderDetailMapper;
import com.pojo.OrderDetail;
import com.service.OrderDetailService;
import com.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/5
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
