package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value ="material_type_208201302")
@Data
public class MaterialType implements Serializable {

//    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
