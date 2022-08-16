package com.cooling.hydraulic.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.cooling.hydraulic.entity.Menu;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.model.menu.MenuDto;
import com.cooling.hydraulic.model.menu.MenuQueryCriteria;
import com.cooling.hydraulic.service.MenuService;
import com.cooling.hydraulic.utils.PageUtil;
import com.cooling.hydraulic.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {


    private static final String ENTITY_NAME = "menu";

    @Resource
    private MenuService menuService;

    @RequestMapping(value = "/download")
    public void exportMenu(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

    @RequestMapping(value = "/build")
    public ResponseEntity<Object> buildMenus(){
        List<MenuDto> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menuDtos = menuService.buildTree(menuDtoList);
        return new ResponseEntity<>(menuService.buildMenus(menuDtos), HttpStatus.OK);
    }

    @RequestMapping(value = "/lazy")
    public ResponseEntity<Object> queryAllMenu(@RequestParam Long pid){
        return new ResponseEntity<>(menuService.getMenus(pid), HttpStatus.OK);
    }

    @RequestMapping(value = "/child")
    public ResponseEntity<Object> childMenu(@RequestParam Long id){
        Set<Menu> menuSet = new HashSet<>();
        List<MenuDto> menuDtoList = menuService.getMenus(id);
        menuSet.add(menuService.findOne(id));
        List<Menu> menuList = menuDtoList.stream().map(menuService::toEntity).collect(Collectors.toList());
        menuSet = menuService.getChildMenus(menuList, menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @RequestMapping(value = "/pageQuery")
    public ResponseEntity<Object> queryMenu(MenuQueryCriteria criteria) throws Exception {
        List<MenuDto> menuDtoList = menuService.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(menuDtoList, menuDtoList.size()), HttpStatus.OK);
    }

    @RequestMapping("/superior")
    public ResponseEntity<Object> getMenuSuperior(@RequestBody List<Long> ids) {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if(CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                MenuDto menuDto = menuService.findById(id);
                menuDtos.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return new ResponseEntity<>(menuService.buildTree(new ArrayList<>(menuDtos)), HttpStatus.OK);
        }
        return new ResponseEntity<>(menuService.getMenus(null), HttpStatus.OK);
    }

    @RequestMapping("/add")
    public ResponseEntity<Object> createMenu(@Validated @RequestBody Menu resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        menuService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/edit")
    public ResponseEntity<Object> updateMenu(@Validated(Menu.Update.class) @RequestBody Menu resources){
        menuService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/del")
    public ResponseEntity<Object> deleteMenu(@RequestBody Set<Long> ids){
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDto> menuDtoList = menuService.getMenus(id);
            menuSet.add(menuService.findOne(id));
            List<Menu> menuList = menuDtoList.stream().map(menuService::toEntity).collect(Collectors.toList());
            menuSet = menuService.getChildMenus(menuList, menuSet);
        }
        menuService.delete(menuSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
