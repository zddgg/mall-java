package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 类目属性库关联表
 *
 * @TableName category_property_sale
 */
@TableName(value = "category_property_sale")
@Getter
@Setter
public class CategoryPropertySale extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 类目编号
     */
    @TableField(value = "category_id")
    private String categoryId;
    /**
     * 属性库编号
     */
    @TableField(value = "property_sale_id")
    private String propertySaleId;
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
}