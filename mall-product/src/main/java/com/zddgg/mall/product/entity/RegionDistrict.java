package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * &#064;TableName  region_district
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "region_district")
@Data
public class RegionDistrict extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableField(value = "district_code")
    private String districtCode;
    /**
     *
     */
    @TableField(value = "district_name")
    private String districtName;
}