package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

public interface DishService {
    //新增菜品和口味
    public void saveWithFlavor(DishDTO dishDTO);

    //分类分页查询
    PageResult page(DishService dishService);
}
