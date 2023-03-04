package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 属性组-属性库关系表
 *
 * @TableName property_group_store
 */
@TableName(value = "property_group_store")
@Data
public class PropertyGroupStore extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性编号
     */
    @TableField(value = "property_group_no")
    private String propertyGroupNo;
    /**
     * 属性名称
     */
    @TableField(value = "property_store_no")
    private String propertyStoreNo;
    /**
     * 属性排序
     */
    @TableField(value = "property_order")
    private Integer propertyOrder;
}