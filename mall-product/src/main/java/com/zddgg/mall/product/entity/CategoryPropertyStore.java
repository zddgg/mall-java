package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 类目属性库关联表
 * @TableName category_property_store
 */
@TableName(value ="category_property_store")
@Data
public class CategoryPropertyStore extends BaseEntity implements Serializable {

    /**
     * 类目编号
     */
    @TableField(value = "category_no")
    private String categoryNo;

    /**
     * 属性库编号
     */
    @TableField(value = "property_store_no")
    private String propertyStoreNo;

    /**
     * 排序
     */
    @TableField(value = "order_no")
    private Integer orderNo;

    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}