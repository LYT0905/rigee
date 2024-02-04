package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.R;
import com.dto.DishDto;
import com.pojo.Category;
import com.pojo.Dish;
import com.pojo.DishFlavor;
import com.service.CategoryService;
import com.service.DishFlavorService;
import com.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark
 * @date 2024/2/3
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>();

        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), "name", name).orderByDesc("update_time");

        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 根据id获取,回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    /**
     * 修改
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishWithFlavors(dishDto);
        return R.success("修改成功");
    }

    /**
     * 删除菜品（单个或者批量）
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.removeByIdWithFlavor(ids);
//        dishService.removeById(ids);
//        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("dish_id", ids);
//        dishFlavorService.remove(queryWrapper);
        return R.success("删除成功");
    }

    /**
     * 根据菜品id查询
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, "category_id", dish.getCategoryId()).
                eq("status", 1).orderByAsc("sort").orderByDesc("update_time");

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 修改菜品售卖状态（单个或者批量）
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", status).in("id", ids);

        if (!dishService.update(updateWrapper)){
            return R.error("修改状态失败，请稍后重试");
        }
        return R.success("修改状态成功");
    }
}
