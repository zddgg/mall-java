package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 属性库value表
 *
 * @TableName property_store_value
 */
@TableName(value = "property_unit_value")
@Getter
@Setter
public class propertyUnitValue extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性key编号
     */
    @TableField(value = "unit_key_id")
    private String unitKeyId;
    /**
     * 属性value
     */
    @TableField(value = "unit_value")
    private String unitValue;
    /**
     * 属性value顺序
     */
    @TableField(value = "unit_value_order")
    private Integer unitValueOrder;
}