package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.R;
import com.dto.SetmealDto;
import com.pojo.Category;
import com.pojo.Setmeal;
import com.service.CategoryService;
import com.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark
 * @date 2024/2/4
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.savemealWithDish(setmealDto);
        return R.success("保存成功");
    }

    /**
     * 分页查询和按名字查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), "name", name).orderByAsc("price").
                orderByDesc("update_time");
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeSermealWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 修改套餐的售卖状态
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status){
        UpdateWrapper<Setmeal> queryWrapper = new UpdateWrapper<>();
        queryWrapper.set("status", status).in("id", ids);

        if (!setmealService.update(queryWrapper)){
            return R.error("修改状态失败，请稍后重试");
        }

        return R.success("修改状态成功");
    }

    /**
     * 根据id获取，进行套餐数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithSetmeal(id);
        return R.success(setmealDto);
    }

    /**
     * 保存修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("保存成功");
    }
}
