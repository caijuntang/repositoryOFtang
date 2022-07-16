package com.cooling.hydraulic.service;

import com.cooling.hydraulic.dao.RoleRepository;
import com.cooling.hydraulic.dao.UserRepository;
import com.cooling.hydraulic.entity.Menu;
import com.cooling.hydraulic.entity.Role;
import com.cooling.hydraulic.entity.User;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.exception.EntityExistException;
import com.cooling.hydraulic.model.menu.MenuDto;
import com.cooling.hydraulic.model.role.RoleDto;
import com.cooling.hydraulic.model.role.RoleQueryCriteria;
import com.cooling.hydraulic.model.role.RoleSmallDto;
import com.cooling.hydraulic.model.user.AuthorityDto;
import com.cooling.hydraulic.model.user.UserDto;
import com.cooling.hydraulic.service.user.UserCacheManagerService;
import com.cooling.hydraulic.utils.PageUtil;
import com.cooling.hydraulic.utils.QueryHelp;
import com.cooling.hydraulic.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private MenuService menuService;

    @Resource
    private UserRepository userRepository;
    @Resource
    private  UserCacheManagerService userCacheManagerService;

    public List<RoleDto> queryAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "level");
        List<Role> roles = roleRepository.findAll(sort);
        return roles.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        List<Role> roles = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return roles.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Object queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        Page<Role> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(this::toDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public RoleDto findById(long id) {
        Role role = roleRepository.findById(id).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", id);
        return toDto(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Role resources) {
        if (roleRepository.findByName(resources.getName()) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        roleRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = roleRepository.findById(resources.getId()).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", resources.getId());
        Role role1 = roleRepository.findByName(resources.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setLevel(resources.getLevel());
        roleRepository.save(role);
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    public void updateMenu(Role resources, RoleDto roleDTO) {
        Role role = toEntity(roleDTO);
        List<User> users = userRepository.findByRoleId(role.getId());
        // 更新菜单
        role.setMenus(resources.getMenus());
        delCaches(resources.getId(), users);
        roleRepository.save(role);
    }

    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long menuId) {
        // 更新菜单
        roleRepository.untiedMenu(menuId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 更新相关缓存
            delCaches(id, null);
        }
        roleRepository.deleteAllByIdIn(ids);
    }

    public List<RoleSmallDto> findByUsersId(Long id) {
        Set<Role> roles = roleRepository.findByUserId(id);
        return  roles.stream().map(this::toSmalDto).collect(Collectors.toList());
    }

    public Integer findByRoles(Set<Role> roles) {
        if (roles.size() == 0) {
            return Integer.MAX_VALUE;
        }
        Set<RoleDto> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDto::getLevel).collect(Collectors.toList()));
    }

    public List<AuthorityDto> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(AuthorityDto::new)
                    .collect(Collectors.toList());
        }
        Set<Role> roles = roleRepository.findByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                .map(Menu::getPermission)
                .filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(AuthorityDto::new)
                .collect(Collectors.toList());
    }

    public void download(List<RoleDto> roles, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (RoleDto role : roles) {
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("角色名称", role.getName());
//            map.put("角色级别", role.getLevel());
//            map.put("描述", role.getDescription());
//            map.put("创建日期", role.getCreateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
    }

    public void verification(Set<Long> ids) {
        if (userRepository.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    public List<Role> findInMenuId(List<Long> menuIds) {
        return roleRepository.findInMenuId(menuIds);
    }

    /**
     * 清理缓存
     * @param id /
     */
    public void delCaches(Long id, List<User> users) {
//        users = CollectionUtil.isEmpty(users) ? userRepository.findByRoleId(id) : users;
//        if (CollectionUtil.isNotEmpty(users)) {
//            users.forEach(item -> userCacheManager.cleanUserCache(item.getUsername()));
//            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
//            redisUtils.delByKeys(CacheKey.DATA_USER, userIds);
//            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
//            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
//        }
//        redisUtils.del(CacheKey.ROLE_ID + id);
    }

    public Role toEntity(RoleDto dto) {
        if ( dto == null ) {
            return null;
        }
        Role role = new Role();
        role.setCreateBy( dto.getCreateBy() );
        role.setUpdateBy( dto.getUpdateBy() );
        role.setCreateTime( dto.getCreateTime() );
        role.setUpdateTime( dto.getUpdateTime() );
        role.setId( dto.getId() );
        Set<MenuDto> menus = dto.getMenus();
        Set<Menu> menus1 = menus.stream().map(menuService::toEntity).collect(Collectors.toSet());
        role.setMenus(menus1);
        role.setName( dto.getName() );
//        role.setDataScope( dto.getDataScope() );
        role.setLevel( dto.getLevel() );
        role.setDescription( dto.getDescription() );
        return role;
    }

    public RoleDto toDto(Role entity) {
        if ( entity == null ) {
            return null;
        }
        RoleDto roleDto = new RoleDto();
        roleDto.setCreateBy( entity.getCreateBy() );
        roleDto.setUpdateBy( entity.getUpdateBy() );
        roleDto.setCreateTime( entity.getCreateTime() );
        roleDto.setUpdateTime( entity.getUpdateTime() );
        roleDto.setId( entity.getId() );
        Set<Menu> menus = entity.getMenus();
        Set<MenuDto> menuDtos = menus.stream().map(menuService::toDto).collect(Collectors.toSet());
        roleDto.setMenus(menuDtos);
        roleDto.setName( entity.getName() );
        roleDto.setLevel( entity.getLevel() );
        roleDto.setDescription( entity.getDescription() );
        return roleDto;
    }

    public RoleSmallDto toSmalDto(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleSmallDto roleSmallDto = new RoleSmallDto();
        roleSmallDto.setId( entity.getId() );
        roleSmallDto.setName( entity.getName() );
        roleSmallDto.setLevel( entity.getLevel() );
        return roleSmallDto;
    }

    public Role roleSmaltoEntity(RoleSmallDto dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();
        role.setId( dto.getId() );
        role.setName( dto.getName() );
        role.setLevel( dto.getLevel() );
        return role;
    }


}
