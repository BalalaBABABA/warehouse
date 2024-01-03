package com.abc.warehouse.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String role;

    private String sex;

    private Date birthDate;

    private String idNumber;

    private String nativePlace;

    private String address;

    private String phone;


}
