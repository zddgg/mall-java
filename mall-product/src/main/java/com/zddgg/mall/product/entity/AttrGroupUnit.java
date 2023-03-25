package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 属性组-属性库关系表
 *
 * @TableName attr_group_unit
 */
@TableName(value = "attr_group_unit")
@Getter
@Setter
public class AttrGroupUnit extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 属性编号
     */
    @TableField(value = "group_id")
    private String groupId;
    /**
     * 属性名称
     */
    @TableField(value = "attr_id")
    private String attrId;
    /**
     * 属性排序
     */
    @TableField(value = "attr_unit_order")
    private Integer attrUnitOrder;
}