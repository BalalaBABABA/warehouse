package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public PermissionType(String type) {
        this.type = type;
    }
}