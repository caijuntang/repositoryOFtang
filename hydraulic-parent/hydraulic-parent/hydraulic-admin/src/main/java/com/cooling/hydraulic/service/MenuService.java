package com.cooling.hydraulic.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cooling.hydraulic.dao.MenuRepository;
import com.cooling.hydraulic.entity.Menu;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.exception.EntityExistException;
import com.cooling.hydraulic.model.menu.MenuDto;
import com.cooling.hydraulic.model.menu.MenuQueryCriteria;
import com.cooling.hydraulic.model.role.RoleSmallDto;
import com.cooling.hydraulic.model.user.JwtUserDto;
import com.cooling.hydraulic.model.vo.MenuMetaVo;
import com.cooling.hydraulic.model.vo.MenuVo;
import com.cooling.hydraulic.utils.QueryHelp;
import com.cooling.hydraulic.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class MenuService {

    @Resource
    private MenuRepository menuRepository;
    @Resource
    private  RoleService roleService;

    private static final Map<String, JwtUserDto> menuCacheMap=new ConcurrentHashMap<String,JwtUserDto>();


    public List<MenuDto> queryAll(MenuQueryCriteria criteria, Boolean isQuery) throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "menuSort");
        if(Boolean.TRUE.equals(isQuery)){
            criteria.setPidIsNull(true);
            List<Field> fields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
            for (Field field : fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if("pidIsNull".equals(field.getName())){
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        List<Menu> menuList = menuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), sort);
        return menuList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public MenuDto findById(long id) {
        Menu menu = menuRepository.findById(id).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(),"Menu","id",id);
        MenuDto menuDto = toDto(menu);
        return menuDto;
    }

    /**
     * 用户角色改变时需清理缓存
     * @param currentUserId /
     * @return /
     */
    public List<MenuDto> findByUser(Long currentUserId) {
        List<RoleSmallDto> roles = roleService.findByUsersId(currentUserId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
        LinkedHashSet<Menu> menus = menuRepository.findByRoleIdsAndTypeNot(roleIds, 2);
        return menus.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Menu resources) {
        if(menuRepository.findByTitle(resources.getTitle()) != null){
            throw new EntityExistException(Menu.class,"title",resources.getTitle());
        }
        if(StringUtils.isNotBlank(resources.getComponentName())){
            if(menuRepository.findByComponentName(resources.getComponentName()) != null){
                throw new EntityExistException(Menu.class,"componentName",resources.getComponentName());
            }
        }
        if(resources.getPid().equals(0L)){
            resources.setPid(null);
        }
        if(resources.getiFrame()){
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http)||resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        menuRepository.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(resources.getPid());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Menu resources) {
        if(resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Menu menu = menuRepository.findById(resources.getId()).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(),"Permission","id",resources.getId());

        if(resources.getiFrame()){
            String http = "http://", https = "https://";
            if (!(resources.getPath().toLowerCase().startsWith(http)||resources.getPath().toLowerCase().startsWith(https))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        Menu menu1 = menuRepository.findByTitle(resources.getTitle());

        if(menu1 != null && !menu1.getId().equals(menu.getId())){
            throw new EntityExistException(Menu.class,"title",resources.getTitle());
        }

        if(resources.getPid().equals(0L)){
            resources.setPid(null);
        }

        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resources.getPid();

        if(StringUtils.isNotBlank(resources.getComponentName())){
            menu1 = menuRepository.findByComponentName(resources.getComponentName());
            if(menu1 != null && !menu1.getId().equals(menu.getId())){
                throw new EntityExistException(Menu.class,"componentName",resources.getComponentName());
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setiFrame(resources.getiFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setComponentName(resources.getComponentName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        menuRepository.save(menu);
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
//        delCaches(resources.getId());
    }

    public Set<Menu> getChildMenus(List<Menu> menuList, Set<Menu> menuSet) {
        for (Menu menu : menuList) {
            menuSet.add(menu);
            List<Menu> menus = menuRepository.findByPidOrderByMenuSort(menu.getId());
            if(menus!=null && menus.size()!=0){
                getChildMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Menu> menuSet) {
        for (Menu menu : menuSet) {
            // 清理缓存
            delCaches(menu.getId());
            roleService.untiedMenu(menu.getId());
            menuRepository.deleteById(menu.getId());
            updateSubCnt(menu.getPid());
        }
    }

    public List<MenuDto> getMenus(Long pid) {
        List<Menu> menus;
        if(pid != null && !pid.equals(0L)){
            menus = menuRepository.findByPidOrderByMenuSort(pid);
        } else {
            menus = menuRepository.findByPidIsNullOrderByMenuSort();
        }
        return menus.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MenuDto> getSuperior(MenuDto menuDto, List<Menu> menus) {
        if(menuDto.getPid() == null){
            menus.addAll(menuRepository.findByPidIsNullOrderByMenuSort());
            if(null!=menus){
                return menus.stream().map(this::toDto).collect(Collectors.toList());
            }
            return null;
        }
        menus.addAll(menuRepository.findByPidOrderByMenuSort(menuDto.getPid()));
        return getSuperior(findById(menuDto.getPid()), menus);
    }

    public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDto menuDTO : menuDtos) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (MenuDto it : menuDtos) {
                if (menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if(trees.size() == 0){
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName())  ? menuDTO.getComponentName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() :menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if(!menuDTO.getIFrame()){
                            if(menuDTO.getPid() == null){
                                menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                                // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                            }else if(menuDTO.getType() == 0){
                                menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"ParentView":menuDTO.getComponent());
                            }else if(StringUtils.isNoneBlank(menuDTO.getComponent())){
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(),menuDTO.getIcon(),!menuDTO.getCache()));
                        if(CollectionUtil.isNotEmpty(menuDtoList)){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if(menuDTO.getPid() == null){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if(!menuDTO.getIFrame()){
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    public Menu findOne(Long id) {
        Menu menu = menuRepository.findById(id).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(),"Menu","id",id);
        return menu;
    }

    public void download(List<MenuDto> menuDtos, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (MenuDto menuDTO : menuDtos) {
//            Map<String,Object> map = new LinkedHashMap<>();
//            map.put("菜单标题", menuDTO.getTitle());
//            map.put("菜单类型", menuDTO.getType() == null ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
//            map.put("权限标识", menuDTO.getPermission());
//            map.put("外链菜单", menuDTO.getIFrame() ? "是" : "否");
//            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
//            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
//            map.put("创建日期", menuDTO.getCreateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
    }

    private void updateSubCnt(Long menuId){
        if(menuId != null){
            int count = menuRepository.countByPid(menuId);
            menuRepository.updateSubCntById(count, menuId);
        }
    }

    /**
     * 清理缓存
     * @param id 菜单ID
     */
    public void delCaches(Long id){
//        List<User> users = userRepository.findByMenuId(id);
//        menuCacheMap.remove(CacheKey.MENU_ID + id);
//        redisUtils.delByKeys(CacheKey.MENU_USER, users.stream().map(User::getId).collect(Collectors.toSet()));
//        // 清除 Role 缓存
//        List<Role> roles = roleService.findInMenuId(new ArrayList<Long>(){{
//            add(id);
//        }});
//        redisUtils.delByKeys(CacheKey.ROLE_ID, roles.stream().map(Role::getId).collect(Collectors.toSet()));
    }

    public Menu toEntity(MenuDto dto) {
        if ( dto == null ) {
            return null;
        }

        Menu menu = new Menu();

        menu.setCreateBy( dto.getCreateBy() );
        menu.setUpdateBy( dto.getUpdateBy() );
        menu.setCreateTime( dto.getCreateTime() );
        menu.setUpdateTime( dto.getUpdateTime() );
        menu.setId( dto.getId() );
        menu.setTitle( dto.getTitle() );
        menu.setComponentName( dto.getComponentName() );
        menu.setMenuSort( dto.getMenuSort() );
        menu.setComponent( dto.getComponent() );
        menu.setPath( dto.getPath() );
        menu.setType( dto.getType() );
        menu.setPermission( dto.getPermission() );
        menu.setIcon( dto.getIcon() );
        menu.setCache( dto.getCache() );
        menu.setHidden( dto.getHidden() );
        menu.setPid( dto.getPid() );
        menu.setSubCount( dto.getSubCount() );
        menu.setiFrame( dto.getIFrame() );

        return menu;
    }

    public MenuDto toDto(Menu entity) {
        if ( entity == null ) {
            return null;
        }
        MenuDto menuDto = new MenuDto();
        menuDto.setCreateBy( entity.getCreateBy() );
        menuDto.setUpdateBy( entity.getUpdateBy() );
        menuDto.setCreateTime( entity.getCreateTime() );
        menuDto.setUpdateTime( entity.getUpdateTime() );
        menuDto.setId( entity.getId() );
        menuDto.setType( entity.getType() );
        menuDto.setPermission( entity.getPermission() );
        menuDto.setTitle( entity.getTitle() );
        menuDto.setMenuSort( entity.getMenuSort() );
        menuDto.setPath( entity.getPath() );
        menuDto.setComponent( entity.getComponent() );
        menuDto.setPid( entity.getPid() );
        menuDto.setSubCount( entity.getSubCount() );
        menuDto.setIFrame( entity.getiFrame() );
        menuDto.setCache( entity.getCache() );
        menuDto.setHidden( entity.getHidden() );
        menuDto.setComponentName( entity.getComponentName() );
        menuDto.setIcon( entity.getIcon() );

        return menuDto;
    }
}
