package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车添加")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //添加购物车
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    //显示查询
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getShoppingCartList(){
        List<ShoppingCart> list = shoppingCartService.getlist();
        return Result.success(list);
    }

    //清空购物车
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.delete();
        return Result.success();
    }

    //单个删除购物车
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.deleteSub(shoppingCartDTO);
        return Result.success();
    }

}
