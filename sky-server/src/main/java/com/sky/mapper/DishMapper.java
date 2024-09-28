package com.sky.mapper;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.nanotation.AotoFill;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    //求取菜品总数
    @Select("select count(*) from dish")
    Integer getCount();

    List<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    @AotoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    //list--id查询
    Dish getListById(Long id);

    List<Dish> list(Dish dish);

    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBysetmaelId(Long setmealId);
}
