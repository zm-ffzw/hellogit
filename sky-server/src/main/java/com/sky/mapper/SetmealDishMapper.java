package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    //查询菜品关联的个数
    List<Long> getSetmealDishIdsBySetmealId(List<Long> dishIds);
}
