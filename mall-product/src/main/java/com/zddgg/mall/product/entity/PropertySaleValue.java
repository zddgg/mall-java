package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 属性库value表
 *
 * @TableName property_sale_value
 */
@TableName(value = "property_sale_value")
@Getter
@Setter
public class PropertySaleValue extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性key编号
     */
    @TableField(value = "sale_key_id")
    private String saleKeyId;
    /**
     * 属性value
     */
    @TableField(value = "property_value")
    private String propertyValue;
    /**
     * 属性value顺序
     */
    @TableField(value = "property_order")
    private Integer propertyOrder;
}