package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 吧啦
 * @description 针对表【user_208201302(人员表)】的数据库操作Service
 * @createDate 2023-09-23 16:58:22
 */
public interface UserService extends IService<User> {

    long getTotalPage();

    Result deleteUserById(Long id);

    Result getNamesAndIds();

    Result getAllUser(Integer pageCount);
}
