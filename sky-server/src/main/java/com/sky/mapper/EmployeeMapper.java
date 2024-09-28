package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.nanotation.AotoFill;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AotoFill(value = OperationType.INSERT)
    void save(Employee employee);

    //获取成员总个数
    @Select("select count(*) from employee")
    public Integer count();

    //List<Employee> getEmplist(Integer start, EmployeePageQueryDTO employeePageQueryDTO);*/

    List<Employee> getEmplist(EmployeePageQueryDTO employeePageQueryDTO);

    @AotoFill(value = OperationType.UPDATE)
    void startOrStop(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    @AotoFill(value = OperationType.INSERT)
    void gainCategory(Category category);

    //@Select("select count(*) from category")
    public Integer countCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO);

    @Delete("delete from category where id = #{id}")
    void deleteCatege(Integer id);

    @AotoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);

    @Update("update category set status = #{status} where id = #{id}")
    void updateCategoryStatus(Integer status, Integer id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);

}
