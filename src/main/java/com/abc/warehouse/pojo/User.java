package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 人员表
 * @TableName user_208201302
 */
@TableName(value ="user_208201302")
@Data
public class User implements Serializable {
    /**
     * 人员代码
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 密码
     */
    private String pwd;

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