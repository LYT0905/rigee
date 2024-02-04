package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.DishFlavorMapper;
import com.pojo.DishFlavor;
import com.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/3
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements  DishFlavorService{
}
