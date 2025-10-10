package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Tag(name = "C端-分类接口")
public class CategoryController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询分类")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = employeeService.list(type);
        return Result.success(list);
    }
}
