package com.abc.warehouse.service.impl;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.UserService;
import com.abc.warehouse.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.abc.warehouse.dto.constants.PageConstants.PERMISSION_SEARCH_PAGE_SIZE;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;


/**
 * @author 吧啦
 * @description 针对表【user_208201302(人员表)】的数据库操作Service实现
 * @createDate 2023-09-23 16:58:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{

    @Resource
    private GenerateID generateID;

    private static final String salt = "wms@#!";
    @Override
    public Result saveUser(User user) {
        long id = generateID.getId("2", "User");
        user.setId(id);
        String password=SaltMD5Util.generateSaltPassword("123456");
//        String password = PasswordEncoder.encode("123456",salt);
        user.setPassword(password);
        if(RegexUtils.isIdNumberInvalid(user.getIdNumber())||RegexUtils.isPhoneInvalid(user.getPhone()))
        {
            return Result.fail("格式错误");
        }
        boolean save = save(user);
        return save?Result.ok():Result.fail("增加权限失败");
    }

    @Override
    public long getTotalPage() {
        //设置分页参数
        Page<User> page =new Page<>(1, PERMISSION_SEARCH_PAGE_SIZE);
        page(page, null);
        return page.getPages();
    }

    @Override
    public Result userPage(Integer curPage) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        IPage<User> pageQuery = new Page<>(curPage, PageConstants.USER_SEARCH_PAGE_SIZE);
        IPage<User> page = baseMapper.selectPage(pageQuery, wrapper);
        System.out.println(page.getRecords().toString());
        return EncryotResult.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result deleteUser(Long id) {
        boolean b = this.removeById(id);
        return b?Result.ok():Result.fail("删除失败");
    }

    @Override
    public Result getNamesAndIds(){
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId,User::getName);
        List<User> list = list(queryWrapper);
        return Result.ok(list);
    }

    @Override
    public Result searchByName(Integer curPage, String name) {
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.like("name", name);
        IPage<User> pageQuery = new Page((long)curPage, (long) PageConstants.USER_SEARCH_PAGE_SIZE);
        IPage<User> page = ((UserMapper)this.baseMapper).selectPage(pageQuery, wrapper);
        return EncryotResult.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result searchById(Integer curPage, Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("id", id);


        IPage<User> pageQuery = new Page((long)curPage, (long)PageConstants.USER_SEARCH_PAGE_SIZE);
        IPage<User> page = ((UserMapper)this.baseMapper).selectPage(pageQuery, wrapper);
        return EncryotResult.ok(page.getRecords(), page.getPages());
    }

    public Result getUserById(Long id)
    {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User user = baseMapper.selectOne(wrapper);
        return Result.ok(user);
    }

    @Override
    public Result updateUser(User user)
    {
        if(RegexUtils.isIdNumberInvalid(user.getIdNumber())||RegexUtils.isPhoneInvalid(user.getPhone()))
        {
            return Result.fail("格式错误");
        }
        updateById(user);
        return Result.ok();
    }

    @Override
    public Result resetPassword(User user)
    {
        String password=SaltMD5Util.generateSaltPassword("123456");
//        String password = PasswordEncoder.encode("123456",salt);
        user.setPassword(password);
        boolean b = updateById(user);
        return b?Result.ok():Result.fail("重置失败");
    }
    @Override
    public Result updatePassword(String token,String newPassword) {
        Long userId = JwtUtils.getUserIdFromToken(token);
        // 获取用户信息
        User user = getById(userId);

        // 进行密码修改
        String password = SaltMD5Util.generateSaltPassword(newPassword);
//        String encryptedPassword = PasswordEncoder.encode(newPassword, salt);
        user.setPassword(password);

        // 更新用户信息
        boolean updateResult = updateById(user);
        return updateResult ? EncryotResult.ok() : EncryotResult.fail("密码修改失败");
    }

    @Override
    public Result updatePhone(String newPhone,String token) {
        Long userId = JwtUtils.getUserIdFromToken(token);
        User user = getById(userId);
        user.setPhone(newPhone);
        boolean updateResult = updateById(user);
        return updateResult ? EncryotResult.ok() : EncryotResult.fail("手机号修改失败");
    }


    @Override
    public List<String> findAllUserName() {
        List<String> userName = list().stream()
                .map(user -> user.getName())
                .collect(Collectors.toList());
        return userName;
    }

    @Override
    public List<User> getAllUser() {
        return list();
    }

    public Result getNowUser(String token)
    {
        Long userId = JwtUtils.getUserIdFromToken(token);
        // 获取用户信息
        User user = getById(userId);
        return Result.ok(user);
    }
}




