package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * &#064;TableName  region_province
 */
@TableName(value = "region_province")
@Getter
@Setter
public class RegionProvince extends BaseEntity implements Serializable {

    /**
     *
     */
    @TableField(value = "province_code")
    private String provinceCode;

    /**
     *
     */
    @TableField(value = "province_name")
    private String provinceName;

    /**
     * 区域信息编号（华东地区等）
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 区域信息名称（华东地区等）
     */
    @TableField(value = "area_name")
    private String areaName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}