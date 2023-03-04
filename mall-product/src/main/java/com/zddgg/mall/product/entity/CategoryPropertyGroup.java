package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 类目属性组关联表
 *
 * @TableName category_property_group
 */
@TableName(value = "category_property_group")
@Data
public class CategoryPropertyGroup extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 类目编号
     */
    @TableField(value = "category_no")
    private String categoryNo;
    /**
     * 属性组编号
     */
    @TableField(value = "property_group_no")
    private String propertyGroupNo;
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