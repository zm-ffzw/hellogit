package com.sky.controller.admin;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    //新增套餐
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache" , key = "#setmealVO.categoryId")
    public Result save(@RequestBody SetmealVO setmealVO) {
        log.info("套餐======：{}", setmealVO);
        setmealService.saveWithDish(setmealVO);
        return Result.success();
    }

    //分页查询
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult setmealPage = setmealService.page(setmealPageQueryDTO);
        return Result.success(setmealPage);
    }

    //根据id进行查询
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    //删除套餐
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache" , allEntries = true)
    public Result delete(@RequestParam List<Long> ids){
        setmealService.deleteSetmealWithDishId(ids);
        return Result.success();
    }

    //修改套餐
    @PutMapping
    @CacheEvict(cacheNames = "setmealCache" , allEntries = true)
    public Result update(@RequestBody SetmealVO setmealVO){
        setmealService.update(setmealVO);
        return Result.success();
    }

    //套餐起售停售
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache" , allEntries = true)
    public Result updateStatus(Long id,@PathVariable Integer status){
        setmealService.updateStatus(id,status);
        return Result.success();
    }
}
