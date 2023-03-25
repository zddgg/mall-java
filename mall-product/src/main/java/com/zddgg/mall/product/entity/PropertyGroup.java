package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 属性组表
 *
 * @TableName property_group
 */
@TableName(value = "property_group")
@Getter
@Setter
public class PropertyGroup extends BaseEntity implements Serializable {

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
    @TableField(value = "property_group_name")
    private String propertyGroupName;
    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;

    @TableField(exist = false)
    private List<AttrUnitKey> AttrUnitKeys;
}