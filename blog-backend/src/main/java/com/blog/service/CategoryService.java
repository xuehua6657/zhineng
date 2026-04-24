package com.blog.service;

import com.blog.entity.Category;

import java.util.List;

public interface CategoryService {

    /** 查询所有分类 */
    List<Category> listAll();

    /** 添加分类 */
    Long add(Category category);
}
