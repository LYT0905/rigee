package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.CustomException;
import com.dto.SetmealDto;
import com.mapper.SetmealMapper;
import com.pojo.Setmeal;
import com.pojo.SetmealDish;
import com.service.SetmealDishService;
import com.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void savemealWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            if (setmealDish != null){
                setmealDish.setSetmealId(setmealDto.getId());
            }
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐（批量和单个）
     * @param ids
     */
    @Override
    @Transactional
    public void removeSermealWithDish(List<Long> ids) {
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids).eq("status", 1);

        int count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("该套餐正在售卖，请先停止售卖");
        }

        this.removeByIds(ids);

        QueryWrapper<SetmealDish> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("setmeal_id", ids);

        setmealDishService.remove(queryWrapper1);
    }

    /**
     * 根据id获取套餐信息，回显
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithSetmeal(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);

        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", setmeal.getId());

        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     * 更新套餐信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }
}
