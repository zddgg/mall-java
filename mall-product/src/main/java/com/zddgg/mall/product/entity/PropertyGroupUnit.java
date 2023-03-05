package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 属性组-属性库关系表
 *
 * @TableName property_group_store
 */
@TableName(value = "property_group_unit")
@Getter
@Setter
public class PropertyGroupUnit extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性编号
     */
    @TableField(value = "property_group_id")
    private String propertyGroupId;
    /**
     * 属性名称
     */
    @TableField(value = "unit_key_id")
    private String unitKeyId;
    /**
     * 属性排序
     */
    @TableField(value = "property_order")
    private Integer propertyOrder;
}