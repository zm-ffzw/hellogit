package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
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

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
