package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限表
 * @TableName permission_208201302
 */
@TableName(value ="permission_208201302")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 人员id
     */
    private Long userId;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 权限类型
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}