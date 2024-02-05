package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pojo.ShoppingCart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mark
 * @date 2024/2/5
 */
public interface ShoppingCartMapping extends BaseMapper<ShoppingCart> {
}
