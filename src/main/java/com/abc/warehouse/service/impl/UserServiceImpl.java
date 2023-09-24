package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.UserService;
import com.abc.warehouse.mapper.UserMapper;
import org.springframework.stereotype.Service;

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
    public long getTotalPage() {
        //设置分页参数
        Page<User> page =new Page<>(1, DEFAULT_PAGE_SIZE);
        page(page, null);
        return page.getPages();
    }

    @Override
    public Result getAllUser() {
        List<User> list = list();
        return Result.ok(list);
    }

    @Override
    public Result deleteUserById(Long id) {
        boolean b = this.removeById(id);
        return b?Result.ok():Result.fail("删除失败");
    }


}




