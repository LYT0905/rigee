package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.CustomException;
import com.mapper.CategoryMapper;
import com.pojo.Category;
import com.pojo.Dish;
import com.pojo.Setmeal;
import com.service.CategoryService;
import com.service.DishService;
import com.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/2
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        // 判断分类下是否关联菜品
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" , id);
        int count1 = dishService.count(queryWrapper);
        if (count1 > 0){
            throw new CustomException("删除失败，当前分类下关联了菜品");
        }
        // 判断分类下是否关联套餐
        QueryWrapper<Setmeal> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", id);
        int count2 = setmealService.count(queryWrapper1);
        if (count2 > 0){
            throw new CustomException("删除失败，当前分类下关联了套餐");
        }
        super.removeById(id);
    }
}
