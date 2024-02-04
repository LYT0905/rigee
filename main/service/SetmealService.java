package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.SetmealDto;
import com.pojo.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mark
 * @date 2024/2/2
 */
@Service
public interface SetmealService extends IService<Setmeal> {
    void savemealWithDish(SetmealDto setmealDto);

    void removeSermealWithDish(List<Long> ids);

    SetmealDto getByIdWithSetmeal(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
