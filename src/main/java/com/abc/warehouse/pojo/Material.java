package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 物料表
 * @TableName material_208201302
 */
@TableName(value ="material_208201302")
@Data
public class Material implements Serializable {
    /**
     * 物料代码
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 物料名称
     */
    private String name;

    /**
     * 仓库名称
     */
    private String houseName;

    /**
     * 规格型号
     */
    private String type;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 备注
     */
    private String comments;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}