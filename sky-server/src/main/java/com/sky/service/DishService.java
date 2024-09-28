package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    //新增菜品和口味
    public void saveWithFlavor(DishDTO dishDTO);

    //分类分页查询
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    //批量删除
    void deleteBatch(List<Long> ids);

    //根据id查询菜品
    DishDTO getById(Long id);

    //修改菜品
    void update(DishDTO dishDTO);

    //修改菜品状态
    void updateStatus(Integer id,Integer status);

    //根据分类id进行查找相关菜品
    List<Dish> getListById(Long categoryId);
}
