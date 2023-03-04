package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 后台类目表
 *
 * @TableName backend_category
 */
@TableName(value = "backend_category")
@Getter
@Setter
public class BackendCategory extends BaseEntity implements Serializable {

    /**
     * 后台类目编号
     */
    @TableField(value = "category_id")
    private String categoryId;

    /**
     * 后台类目名称
     */
    @TableField(value = "category_name")
    private String categoryName;

    /**
     * 父类目编号
     */
    @TableField(value = "parent_id")
    private String parentId;

    /**
     * 后台类目层级
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 后台类目状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;

    /**
     * 后台类目状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "sort")
    private Integer sort;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}