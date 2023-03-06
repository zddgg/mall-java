package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 类目属性组关联表
 *
 * @TableName category_property_group
 */
@TableName(value = "category_property_group")
@Getter
@Setter
public class CategoryPropertyGroup extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 类目编号
     */
    @TableField(value = "category_id")
    private String categoryId;
    /**
     * 属性组编号
     */
    @TableField(value = "property_group_id")
    private String propertyGroupId;
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