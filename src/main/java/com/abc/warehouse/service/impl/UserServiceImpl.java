package com.abc.warehouse.service.impl;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.utils.RegexUtils;
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
import java.util.List;


import static com.abc.warehouse.utils.SystemConstants.DEFAULT_PAGE_SIZE;

/**
 * @author 吧啦
 * @description 针对表【user_208201302(人员表)】的数据库操作Service实现
 * @createDate 2023-09-23 16:58:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{

    @Override
    public Result saveUser(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
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
        Page<User> page =new Page<>(1, DEFAULT_PAGE_SIZE);
        page(page, null);
        return page.getPages();
    }

    @Override
    public Result userPage(Integer curPage) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        IPage<User> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<User> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
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
        wrapper.eq("name", name);
        IPage<User> pageQuery = new Page((long)curPage, (long) PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<User> page = ((UserMapper)this.baseMapper).selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result searchById(Integer curPage, Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("id", id);
        IPage<User> pageQuery = new Page((long)curPage, (long)PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<User> page = ((UserMapper)this.baseMapper).selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
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

}




