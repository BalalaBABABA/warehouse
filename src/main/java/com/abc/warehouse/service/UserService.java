package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 吧啦
 * @description 针对表【user_208201302(人员表)】的数据库操作Service
 * @createDate 2023-09-23 16:58:22
 */
public interface UserService extends IService<User> {

    Result saveUser(User user);

    long getTotalPage();

    Result deleteUser(Long id);

    Result getNamesAndIds();

    Result userPage(Integer pageCount);

    Result searchByName(Integer curPage, String name);

    Result searchById(Integer curPage, Long id);

    Result updateUser(User user);

    Result resetPassword(User user);
    Result updatePassword(String token,String newPassword);

    Result updatePhone(String newPhone,String token);

    List<String> findAllUserName();

    List<User> getAllUser();

    Result getNowUser(String token);

    Result getUserById(Long id);
}
