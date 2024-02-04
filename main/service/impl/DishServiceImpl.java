package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.CustomException;
import com.dto.DishDto;
import com.mapper.DishMapper;
import com.pojo.Dish;
import com.pojo.DishFlavor;
import com.service.DishFlavorService;
import com.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/2
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 保存菜品基本信息和口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id获取菜品相关信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish, dishDto);

        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }

    /**
     * 根据id更新菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateDishWithFlavors(DishDto dishDto) {
        this.updateById(dishDto);

        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void removeByIdWithFlavor(List<Long> ids) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids).eq("status", 1);

        int count = this.count(queryWrapper);

        if (count > 0){
            throw new CustomException("该菜品正在售卖，请先停售再进行操作");
        }

        this.removeByIds(ids);

        QueryWrapper<DishFlavor> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("dish_id", ids);

        dishFlavorService.remove(queryWrapper1);
    }
}
