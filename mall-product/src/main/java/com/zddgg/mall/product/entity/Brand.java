package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 品牌表
 *
 * @TableName brand
 */
@TableName(value = "brand")
@Getter
@Setter
public class Brand extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 品牌编号
     */
    @TableField(value = "brand_id")
    private String brandId;
    /**
     * 中文品牌名称
     */
    @TableField(value = "brand_name")
    private String brandName;
    /**
     * 外文品牌名称
     */
    @TableField(value = "foreign_brand_name")
    private String foreignBrandName;
    /**
     * 品牌展示名称
     */
    @TableField(value = "show_name")
    private String showName;
    /**
     * 品牌分级:0-未分级，1-B级，2-A级，3-S级
     */
    @TableField(value = "level")
    private String level;
    /**
     * 品牌LOGO图片URL
     */
    @TableField(value = "logo")
    private String logo;
    /**
     * 品牌关联商标名称，使用json格式存储
     */
    @TableField(value = "trademark_name")
    private String trademarkName;
    /**
     * 品牌状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "status")
    private String status;
}