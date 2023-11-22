package com.abc.warehouse.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 不包含pwd属性，保护隐私
 */
@Data
public class UserDTO {
    /**
     * 人员代码
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色
     */
    private String role;

    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;


}
