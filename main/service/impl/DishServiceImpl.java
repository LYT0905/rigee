package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.DishMapper;
import com.pojo.Dish;
import com.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/2
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}
