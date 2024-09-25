package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, long id);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);

    void gainCategory(CategoryDTO categoryDTO);

    PageResult pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    void deleteCategory(Integer id);

    void updateCategory(CategoryDTO categoryDTO);

    void updateCategoryStatus(Integer status, Integer id);


    List<Category> list(Integer type);
}
