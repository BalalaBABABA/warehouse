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
 * @TableName store_208201302
 */
@TableName(value ="store_208201302")
@Data
public class Store implements Serializable {
    /**
     * 进仓单号
     */
//    @TableId(type = IdType.AUTO)
    private Long storeId;

    /**
     * 仓库代码
     */
    private Long houseId;

    /**
     * 进仓时间
     */
    private Date storeTime;

    /**
     * 物料代码
     */
    private Long materialId;

    /**
     * 进仓数量
     */
    private Integer storeCount;

    /**
     * 操作人员代码
     */
    private Long userId;

    /**
     * 备注
     */
    private String notes;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}