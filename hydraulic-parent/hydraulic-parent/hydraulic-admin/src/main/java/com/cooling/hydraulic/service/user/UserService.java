package com.cooling.hydraulic.service.user;

import com.cooling.hydraulic.dao.UserRepository;
import com.cooling.hydraulic.entity.Role;
import com.cooling.hydraulic.entity.User;
import com.cooling.hydraulic.exception.EntityExistException;
import com.cooling.hydraulic.exception.EntityNotFoundException;
import com.cooling.hydraulic.model.role.RoleDto;
import com.cooling.hydraulic.model.role.RoleSmallDto;
import com.cooling.hydraulic.model.user.UserDto;
import com.cooling.hydraulic.model.user.UserLoginDto;
import com.cooling.hydraulic.service.RoleService;
import com.cooling.hydraulic.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private  OnlineUserService onlineUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserCacheManagerService userCacheManagerService;

    @Autowired
    private UserRepository userRepository;

    private  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(), "User", "id", id);
        return toDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(User resources) {
        if (userRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
//        if (userRepository.findByEmail(resources.getEmail()) != null) {
//            throw new EntityExistException(User.class, "email", resources.getEmail());
//        }
        if (userRepository.findByPhone(resources.getPhone()) != null) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        userRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) throws Exception {
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(), "User", "id", resources.getId());
        User user1 = userRepository.findByUsername(resources.getUsername());
//        User user2 = userRepository.findByEmail(resources.getEmail());
        User user3 = userRepository.findByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
//        if (user2 != null && !user.getId().equals(user2.getId())) {
//            throw new EntityExistException(User.class, "email", resources.getEmail());
//        }
        if (user3 != null && !user.getId().equals(user3.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        // 如果用户的角色改变
//        if (!resources.getRoles().equals(user.getRoles())) {
//            redisUtils.del(CacheKey.DATA_USER + resources.getId());
//            redisUtils.del(CacheKey.MENU_USER + resources.getId());
//            redisUtils.del(CacheKey.ROLE_AUTH + resources.getId());
//        }
//        // 修改部门会影响 数据权限
//        if (!Objects.equals(resources.getDept(),user.getDept())) {
//            redisUtils.del(CacheKey.DATA_USER + resources.getId());
//        }
        // 如果用户被禁用，则清除用户登录信息
        if(!resources.getEnabled()){
            onlineUserService.kickOutForUsername(resources.getUsername());
        }
        user.setUsername(resources.getUsername());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        user.setPhone(resources.getPhone());
        userRepository.save(user);
        // 清除缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(User resources) {
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        User user1 = userRepository.findByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        user.setPhone(resources.getPhone());
        userRepository.save(user);
        // 清理缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            UserDto user = findById(id);
            delCaches(user.getId(), user.getUsername());
        }
        userRepository.deleteAllByIdIn(ids);
    }

    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return toDto(user);
        }
    }

    public UserLoginDto getLoginData(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return toUserLoginDto(user);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username, pass, new Date());
        flushCache(username);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
//        // 文件大小验证
//        FileUtil.checkSize(properties.getAvatarMaxSize(), multipartFile.getSize());
//        // 验证文件上传的格式
//        String image = "gif jpg png jpeg";
//        String fileType = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
//        if(fileType != null && !image.contains(fileType)){
//            throw new BadRequestException("文件格式错误！, 仅支持 " + image +" 格式");
//        }
//        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
//        String oldPath = user.getAvatarPath();
//        File file = FileUtil.upload(multipartFile, properties.getPath().getAvatar());
//        user.setAvatarPath(Objects.requireNonNull(file).getPath());
//        user.setAvatarName(file.getName());
//        userRepository.save(user);
//        if (StringUtils.isNotBlank(oldPath)) {
//            FileUtil.del(oldPath);
//        }
//         String username = user.getUsername();
//        flushCache(username);
//        return new HashMap<String, String>(1) {{
//            put("avatar", file.getName());
//        }};
        return null;
    }

//    public void download(List<UserDto> queryAll, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (UserDto userDTO : queryAll) {
//            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDto::getName).collect(Collectors.toList());
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("用户名", userDTO.getUsername());
//            map.put("角色", roles);
//            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
//            map.put("手机号码", userDTO.getPhone());
//            map.put("修改密码的时间", userDTO.getPwdResetTime());
//            map.put("创建日期", userDTO.getCreateTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
//    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, String username) {
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheManagerService.cleanUserCache(username);
    }

    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }
        User user = new User();
        user.setCreateBy( dto.getCreateBy() );
        user.setUpdateBy( dto.getUpdateBy() );
        user.setCreateTime( dto.getCreateTime() );
        user.setUpdateTime( dto.getUpdateTime() );
        user.setId(dto.getId());
        Set<Role> roleSet = dto.getRoles().stream().map(roleService::roleSmaltoEntity).collect(Collectors.toSet());
        user.setRoles(roleSet);
        user.setUsername( dto.getUsername() );
        user.setPhone( dto.getPhone() );
        user.setPassword( dto.getPassword() );
        user.setEnabled( dto.getEnabled() );
        user.setAdmin( dto.getIsAdmin() );
        user.setPwdResetTime( dto.getPwdResetTime() );

        return user;
    }

    public UserDto toDto(User entity) {
        if ( entity == null ) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setCreateBy( entity.getCreateBy() );
        userDto.setUpdateBy( entity.getUpdateBy() );
        userDto.setCreateTime( entity.getCreateTime() );
        userDto.setUpdateTime( entity.getUpdateTime() );
        userDto.setId( entity.getId() );
        Set<Role> roles = entity.getRoles();
        Set<RoleSmallDto> roleSmallDtos = roles.stream().map(roleService::toSmalDto).collect(Collectors.toSet());
        userDto.setRoles(roleSmallDtos);
        userDto.setUsername( entity.getUsername() );
        userDto.setPhone( entity.getPhone() );
        userDto.setPassword( entity.getPassword() );
        userDto.setEnabled( entity.getEnabled() );
        userDto.setIsAdmin( entity.getAdmin() );
        userDto.setPwdResetTime( entity.getPwdResetTime() );
        return userDto;
    }

    public UserLoginDto toUserLoginDto(User entity) {
        if ( entity == null ) {
            return null;
        }
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setCreateBy( entity.getCreateBy() );
        userLoginDto.setUpdateBy( entity.getUpdateBy() );
        userLoginDto.setCreateTime( entity.getCreateTime() );
        userLoginDto.setUpdateTime( entity.getUpdateTime() );
        userLoginDto.setId( entity.getId() );
        Set<RoleSmallDto> roleSmallDtos = entity.getRoles().stream().map(roleService::toSmalDto).collect(Collectors.toSet());
        userLoginDto.setRoles( roleSmallDtos);
        userLoginDto.setUsername( entity.getUsername() );
        userLoginDto.setPhone( entity.getPhone() );
        userLoginDto.setPassword( entity.getPassword() );
        userLoginDto.setEnabled( entity.getEnabled() );
        userLoginDto.setIsAdmin( entity.getAdmin() );
        userLoginDto.setPwdResetTime( entity.getPwdResetTime() );

        return userLoginDto;
    }

}
