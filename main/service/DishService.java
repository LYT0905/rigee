package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.DishDto;
import com.pojo.Dish;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/2
 */

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateDishWithFlavors(DishDto dishDto);

    void removeByIdWithFlavor(List<Long> ids);
}
