package com.zddgg.mall.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺信息表
 * @TableName store_meta
 */
@TableName(value ="store_meta")
@Data
public class StoreMeta implements Serializable {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建者
     */
    @TableField(value = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 更新者
     */
    @TableField(value = "updater")
    private String updater;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    private Date updated;

    /**
     * 店铺编号
     */
    @TableField(value = "store_id")
    private String storeId;

    /**
     * 店铺名称
     */
    @TableField(value = "store_name")
    private String storeName;

    /**
     * 商户编号
     */
    @TableField(value = "mer_id")
    private String merId;

    /**
     * 状态标识符 0:启用 1:禁用 2:删除
     */
    @TableField(value = "state_flag")
    private String stateFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}