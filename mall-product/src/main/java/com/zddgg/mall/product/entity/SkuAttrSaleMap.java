package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 后台类目表
 *
 * @TableName spu_attr_sale_map
 */
@TableName(value = "sku_attr_sale_map")
@Getter
@Setter
public class SkuAttrSaleMap extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "spu_id")
    private String spuId;

    @TableField(value = "sku_id")
    private String skuId;

    @TableField(value = "attr_id")
    private String attrId;

    @TableField(value = "attr_value_id")
    private String attrValueId;

    @TableField(value = "attr_value_name")
    private String attrValueName;

    @TableField(value = "status_flag")
    private String statusFlag;
}