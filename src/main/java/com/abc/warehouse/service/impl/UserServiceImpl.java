package com.abc.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.UserService;
import com.abc.warehouse.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 吧啦
* @description 针对表【user_208201302(人员表)】的数据库操作Service实现
* @createDate 2023-09-19 15:31:43
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




