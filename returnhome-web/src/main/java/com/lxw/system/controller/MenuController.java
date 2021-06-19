package com.lxw.system.controller;


import com.lxw.entity.Menu;
import com.lxw.response.Result;
import com.lxw.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lxw
 * @since 2021-03-11
 */
@Api(tags = "菜单接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    /**
     * 获取菜单
     * @return
     */
    @ApiOperation(value = "加载菜单",notes = "登入成功后加载该用户的菜单")
    @GetMapping
    public Result getMenuList(){
        List<Menu> menuList = menuService.getMenuList();
        return Result.success().data("menu",menuList);
    }




}

