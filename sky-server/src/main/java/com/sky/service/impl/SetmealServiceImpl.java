package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Transactional
    public void saveWithDish(SetmealVO setmealVO) {
        //添加套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO,setmeal);
        setmealMapper.save(setmeal);
        //添加的相关菜品并设置对应id
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        setmealDishes.forEach(setmealDish ->
        {setmealDish.setSetmealId(setmealId);
            setmealDishMapper.gainSetmealDish(setmealDish);
        });
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        Integer count = setmealMapper.count();
        setmealPageQueryDTO.setPage(setmealPageQueryDTO.getPage() - 1);
        List<Setmeal> setmeals = setmealMapper.page(setmealPageQueryDTO);
        return new PageResult(count,setmeals);
    }

    public SetmealVO getById(Long id) {
        //获取基本套餐信息
        Setmeal setmeal = setmealMapper.getById(id);
        //获取对应菜品信息
        List<SetmealDish> list = setmealDishMapper.getSetmealdishByDishId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    @Transactional
    public void deleteSetmealWithDishId(List<Long> ids) {
        //判断启用禁用
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        ids.forEach(setmealId -> {
            //删除基本套餐
            setmealMapper.deleteSetmeal(setmealId);
            //删除相连的菜品
            setmealDishMapper.deleteSetmealWithDish(setmealId);
        });
    }

    @Override
    public void update(SetmealVO setmealVO) {
        //修改套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO,setmeal);
        setmealMapper.update(setmeal);
        //删除相关菜品信息
        Long setmealId = setmeal.getId();
        List<SetmealDish> list = setmealVO.getSetmealDishes();
        setmealDishMapper.deleteSetmealWithDish(setmealId);
        //添加菜品信息
        list.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.gainSetmealDish(setmealDish);
        });

    }

    @Override
    public void updateStatus(Long id, Integer status) {
        //判断其中菜品状态如果起售则setmwal则不能起售
        if(status == StatusConstant.ENABLE){
            List<Dish> list = dishMapper.getBysetmaelId(id);
            if (list != null && list.size() > 0) {
                list.forEach(dish -> {
                    if(dish.getStatus() == StatusConstant.DISABLE){
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = new Setmeal().builder().
                id(id).
                status(status).
                build();
        setmealMapper.update(setmeal);
    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
