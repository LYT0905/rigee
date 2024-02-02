package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.R;
import com.pojo.Category;
import com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mark
 * @date 2024/2/2
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
}
