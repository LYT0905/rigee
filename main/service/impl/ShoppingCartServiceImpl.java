package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.ShoppingCartMapping;
import com.pojo.ShoppingCart;
import com.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/5
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapping, ShoppingCart> implements ShoppingCartService {
}
