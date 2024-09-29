package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);

        //清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanRedis("key");

        return Result.success();
    }

    //分业分类查询
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    //删除
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);

        //清理缓存
        cleanRedis("dish_*");

        return Result.success();
    }

    //根据id查询菜品
    @GetMapping("/{id}")
    public Result<DishDTO> getById(@PathVariable Long id){
        DishDTO dish = dishService.getById(id);
        return Result.success(dish);
    }

    //修改菜品分类
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);

        //清理缓存
        cleanRedis("dish_*");

        return Result.success();
    }

    //修改菜品起售停售
    @PostMapping("/status/{status}")
    public Result updateStatus(Integer id,@PathVariable Integer status){
        dishService.updateStatus(id,status);

        //清理缓存
        cleanRedis("dish_*");

        return Result.success();
    }

    //分类id查询菜品
    @GetMapping("/list")
    public Result<List<Dish>> getListById(Long categoryId){
        List<Dish> list = dishService.getListById(categoryId);
        return Result.success(list);
    }

    //清理缓存数据
    public void cleanRedis(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
