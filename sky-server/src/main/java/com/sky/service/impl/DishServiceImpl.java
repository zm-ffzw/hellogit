package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.nanotation.AotoFill;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.apache.logging.log4j.spi.CopyOnWrite;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //向菜品加入1条数据
        dishMapper.insert(dish);
        Long id = dish.getId();

        //加入n条口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    //分类分页查询
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        Integer count = dishMapper.getCount();
        dishPageQueryDTO.setPage((dishPageQueryDTO.getPage() - 1)* dishPageQueryDTO.getPageSize());
        List<DishVO> dishVOS = dishMapper.page(dishPageQueryDTO);
        return new PageResult(count,dishVOS);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断启用和禁用
        for (long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //菜品起售中，不能进行删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断是否关联菜品
        List<Long> setmealIds = setmealDishMapper.getSetmealDishIdsBySetmealId(ids);
        if(setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //进行批量删除
        for (Long id : ids) {
            dishMapper.deleteById(id);
            //删除对应的口味
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    @Override
    public DishDTO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish,dishDTO);
        //根据id进行查找相应的口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.getById(id);
        dishDTO.setFlavors(dishFlavors);
        return dishDTO;
    }


    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品基本信息
        dishMapper.update(dish);
        //删除对应口味表
        dishFlavorMapper.deleteByDishId(dish.getId());
        //添加口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }


    public void updateStatus(Integer id,Integer status) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(Integer.toUnsignedLong(id));
        dishMapper.update(dish);
    }

    //分类id查找菜品
    public List<Dish> getListById(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}
