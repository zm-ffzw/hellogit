package com.sky.mapper;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.nanotation.AotoFill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {
    @AotoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    //获取套餐总数
    @Select("select count(*) from setmeal")
    Integer count();

    List<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    //批量删除setmeal基本信息
    @Delete("delete from setmeal where id = #{id}")
    void deleteSetmeal(Long id);

    @AotoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    //@Update("update setmeal set ")
    //void updateStatus(Long id, Integer status);
}
