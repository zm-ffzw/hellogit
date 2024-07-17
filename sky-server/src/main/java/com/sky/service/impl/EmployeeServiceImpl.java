package com.sky.service.impl;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.apache.http.HttpRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.IdGenerator;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private HttpServletRequest request;

    private String secretKey;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //
        //更该密码格式
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //属性拷贝，将DTO中的数据拷贝到employee中
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置账号状态
        employee.setStatus(StatusConstant.ENABLE);

        //设置更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置初是密码并d5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //创建人id，修改人id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        //调用mapper接口将数据添加到数据库
        employeeMapper.save(employee);

    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        Integer count = employeeMapper.count();
        employeePageQueryDTO.setPage(employeePageQueryDTO.getPage() - 1);
        /*System.out.println("Name: " + employeePageQueryDTO.getName());
        System.out.println("Page: " + employeePageQueryDTO.getPage());
        System.out.println("PageSize: " + employeePageQueryDTO.getPageSize());*/
        List<Employee> records = employeeMapper.getEmplist(employeePageQueryDTO);
        return new PageResult(count,records);
    }

}
