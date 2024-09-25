package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class CategoryMapping {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/category")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        employeeService.gainCategory(categoryDTO);
        return Result.success();
    }

    //分类分页查询
    @GetMapping("/category/page")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult = employeeService.pageQueryCategory(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    //根据id进行删除
    @DeleteMapping("/category")
    public Result deleteCategory(Integer id){
        employeeService.deleteCategory(id);
        return Result.success();
    }

    //修改分类
    @PutMapping("/category")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        employeeService.updateCategory(categoryDTO);
        return Result.success();
    }

    //启用禁用
    @PostMapping("/category/status/{status}")
    public Result updateCategoryStatus(@PathVariable Integer status,Integer id){
        employeeService.updateCategoryStatus(status,id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/category/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        List<Category> list = employeeService.list(type);
        return Result.success(list);
    }

}
