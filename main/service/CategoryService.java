package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pojo.Category;

/**
 * @author Mark
 * @date 2024/2/2
 */
public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
