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
 * @TableName property_store_key
 */
@TableName(value = "property_unit_key")
@Getter
@Setter
public class PropertyUnitKey extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性key编号
     */
    @TableField(value = "unit_key_id")
    private String unitKeyId;
    /**
     * 属性key名称
     */
    @TableField(value = "unit_key_name")
    private String unitKeyName;
    /**
     * 属性key单位
     */
    @TableField(value = "unit_key_unit")
    private String unitKeyUnit;
    /**
     * 表单展示方式
     */
    @TableField(value = "form_show_type")
    private String formShowType;
    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;
    @TableField(exist = false)
    private List<propertyUnitValue> propertyUnitValues;
}