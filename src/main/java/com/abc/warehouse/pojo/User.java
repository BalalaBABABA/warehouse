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

    @TableId(type = IdType.AUTO)
    private Long id;
    private String password;
    private String name;
    private String role;
    private String sex;
    private Date birthDate;
    private String idNumber;
    private String nativePlace;
    private String address;
    private String phone;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}