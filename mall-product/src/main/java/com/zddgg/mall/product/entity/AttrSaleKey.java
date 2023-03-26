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
 * @TableName attr_sale_key
 */
@TableName(value = "attr_sale_key")
@Getter
@Setter
public class AttrSaleKey extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性key编号
     */
    @TableField(value = "attr_id")
    private String attrId;
    /**
     * 属性key名称
     */
    @TableField(value = "attr_name")
    private String attrName;
    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;

    @TableField(exist = false)
    private List<AttrSaleValue> attrSaleValues;
}