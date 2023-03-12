package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@TableName(value = "spu_meta")
@Getter
@Setter
public class SpuMeta extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "spu_id")
    private String spuId;

    @TableField(value = "spu_name")
    private String spuName;

    @TableField(value = "store_id")
    private String storeId;

    @TableField(value = "brand_id")
    private String brandId;

    @TableField(value = "category_id")
    private String categoryId;

    @TableField(value = "status_flag")
    private String statusFlag;
}
