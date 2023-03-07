package com.zddgg.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@TableName(value = "store_meta")
@Getter
@Setter
public class StoreMeta extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(value = "mer_no")
    private String merNo;

    @TableField(value = "store_no")
    private String storeNo;

    @TableField(value = "store_name")
    private String storeName;

    @TableField(value = "status_flag")
    private String statusFlag;
}
