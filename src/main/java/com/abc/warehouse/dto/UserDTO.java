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
    private String userName;

    /**
     * 角色
     */
    private String role;

    /**
     * 性别
     */
    private Object gender;

    /**
     * 出生日期
     */
    private Date birthdate;

    /**
     * 身份证号
     */
    private String idnumber;

    /**
     * 籍贯
     */
    private String nativeplace;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phonenumber;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
