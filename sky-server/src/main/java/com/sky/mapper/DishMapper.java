package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.nanotation.AotoFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    @AotoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}
