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

    @TableField(value = "spu_no")
    private String spuNo;

    @TableField(value = "spu_name")
    private String spuName;

    @TableField(value = "store_no")
    private String storeNo;

    @TableField(value = "brand_no")
    private String brandNo;

    @TableField(value = "first_category_no")
    private String firstCategoryNo;

    @TableField(value = "second_category_no")
    private String secondCategoryNo;

    @TableField(value = "third_category_no")
    private String thirdCategoryNo;

    @TableField(value = "status_flag")
    private String statusFlag;
}
