package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @TableName region_city
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "region_city")
@Data
public class RegionCity extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableField(value = "city_code")
    private String cityCode;
    /**
     *
     */
    @TableField(value = "city_name")
    private String cityName;
}