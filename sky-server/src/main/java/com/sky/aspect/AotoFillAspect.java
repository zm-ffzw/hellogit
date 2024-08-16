package com.sky.aspect;

import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import com.sky.nanotation.AotoFill;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

//自定义注解，公共字段自动填充
@Aspect
@Component
@Slf4j
public class AotoFillAspect {

    //设置切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.nanotation.AotoFill)")
    public void aotoFillPointCut(){};

    //设置通知
    @Before("aotoFillPointCut()")
    public void aotoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始公共字段填充----------");

        //获取当前被拦截的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AotoFill aotoFill = signature.getMethod().getAnnotation(AotoFill.class);//获取方法上的注解对象
        OperationType value = aotoFill.value();//获取数据库操作类型


        //获取到当前拦截的方法参数--实体对象
        Object[] args = joinPoint.getArgs();//获取实体对象
        if(args.length == 0 || args == null){
            return;
        }
        Object entity = args[0];//默认将主实体对象为第一个参数

        //准备需要赋值的值
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同的操作类型，为对应的属性进行反射来赋值
        if(value == OperationType.INSERT){
            //为四个属性进行赋值

                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);

        }else if(value == OperationType.UPDATE){
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }
    }
}
