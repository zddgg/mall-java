package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value = "sku_meta")
@Getter
@Setter
public class SkuMeta extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "spu_id")
    private String spuId;

    @TableField(value = "sku_id")
    private String skuId;

    @TableField(value = "sku_name")
    private String skuName;

    @TableField(value = "retail_price")
    private BigDecimal retailPrice;

    @TableField(value = "status_flag")
    private String statusFlag;
}
