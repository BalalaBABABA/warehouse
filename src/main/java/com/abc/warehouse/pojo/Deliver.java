package com.abc.warehouse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName deliver_208201302
 */
@TableName(value ="deliver_208201302")
@Data
public class Deliver implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long deliverId;

    /**
     * 出仓单号
     */
    private Long deliverNo;

    /**
     * 仓库代码
     */
    private String houseName;

    /**
     * 出仓时间
     */
    private Date deliverTime;

    /**
     * 物料代码
     */
    private Long materialId;

    /**
     * 出仓数量
     */
    private Integer deliverCount;

    private Integer remainCount = 0;

    /**
     * 操作人员代码
     */
    private Long userId;

    /**
     * 备注
     */
    private String notes;

    @TableField(exist = false)
    private Integer stock;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}