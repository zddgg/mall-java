package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 属性库value表
 *
 * @TableName attr_sale_value
 */
@TableName(value = "attr_sale_value")
@Getter
@Setter
public class AttrSaleValue extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性key编号
     */
    @TableField(value = "attr_id")
    private String attrId;

    @TableField(value = "attr_value_id")
    private String attrValueId;
    /**
     * 属性value
     */
    @TableField(value = "attr_value_name")
    private String attrValueName;
    /**
     * 属性value顺序
     */
    @TableField(value = "attr_value_order")
    private Integer attrValueOrder;
}