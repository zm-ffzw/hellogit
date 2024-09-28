package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    //新增套餐
    void saveWithDish(SetmealVO setmealVO);

    //分页查询
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    //根据id查找
    SetmealVO getById(Long id);

    //删除套餐并删除相关dishId
    void deleteSetmealWithDishId(List<Long> ids);

    //修改套餐
    void update(SetmealVO setmealVO);

    void updateStatus(Long id, Integer status);
}
