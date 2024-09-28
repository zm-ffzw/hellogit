package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    @Insert("insert into setmeal_dish(setmeal_id, dish_id, name, price, copies) values " +
            "(#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void gainSetmealDish(SetmealDish setmealDish);

    //查询菜品关联的个数
    List<Long> getSetmealDishIdsBySetmealId(List<Long> dishIds);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getSetmealdishByDishId(Long setmealId);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteSetmealWithDish(Long setmealId);
}
