package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 资源表
 * @TableName resource_208201302
 */
@TableName(value ="resource_208201302")
@Data
@AllArgsConstructor
public class Resource implements Serializable {
    /**
     * 资源代码
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源名称
     */
    private String name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}