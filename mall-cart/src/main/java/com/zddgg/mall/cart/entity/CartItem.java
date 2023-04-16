package com.zddgg.mall.cart.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车
 *
 * @TableName cart_item
 */
@TableName(value = "cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity implements Serializable {

    /**
     * 购物车编号
     */
    @TableField(value = "cart_id")
    private String cartId;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 店铺编号
     */
    @TableField(value = "store_id")
    private String storeId;

    /**
     * SKU编号
     */
    @TableField(value = "sku_id")
    private String skuId;

    /**
     * SKU数量
     */
    @TableField(value = "sku_num")
    private Integer skuNum;

    /**
     * 加购价格
     */
    @TableField(value = "add_price")
    private BigDecimal addPrice;

    /**
     * 是否选中: 1-是, 0-否
     */
    @TableField(value = "selected_flag")
    private String selectedFlag;

    /**
     * 标识符 -1:删除 0:启用 1:禁用
     */
    @TableField(value = "state_flag")
    private String stateFlag;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}