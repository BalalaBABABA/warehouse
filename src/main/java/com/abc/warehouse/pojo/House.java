package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName house_208201302
 */
@TableName(value ="house_208201302")
@Data
public class House implements Serializable {
    /**
     * 仓库代码
     */
//    @TableId(type = IdType.AUTO)
    private Long houseId;

    /**
     * 仓库名称
     */
    private String houseName;

    /**
     * 仓库地址
     */
    private String houseAdd;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}