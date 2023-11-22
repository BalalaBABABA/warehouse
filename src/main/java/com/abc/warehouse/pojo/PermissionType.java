package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @TableName permission_type_208201302
 */
@TableName(value ="permission_type_208201302")
@Data
@AllArgsConstructor
public class PermissionType implements Serializable {


    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long resourceId;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String uri;
    /**
     * 表示是否展示
     */
    private Integer isdisplay;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public PermissionType(String type) {
        this.type = type;
    }
    public PermissionType(Long id, Long resourceId) {
        this.id = id;
        this.resourceId = resourceId;
    }
}