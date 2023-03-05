package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 属性库key表
 *
 * @TableName property_sale_key
 */
@TableName(value = "property_sale_key")
@Getter
@Setter
public class PropertySaleKey extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性key编号
     */
    @TableField(value = "key_id")
    private String keyId;
    /**
     * 属性key名称
     */
    @TableField(value = "key_name")
    private String keyName;
    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;

    @TableField(exist = false)
    private List<PropertySaleValue> propertySaleValues;
}