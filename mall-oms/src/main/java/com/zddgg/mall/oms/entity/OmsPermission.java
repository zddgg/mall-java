package com.zddgg.mall.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * OMS资源表
 *
 * @TableName oms_permission
 */
@TableName(value = "oms_permission")
@Getter
@Setter
public class OmsPermission extends BaseEntity implements Serializable {

    /**
     * 资源编号
     */
    @TableField(value = "permission_id")
    private String permissionId;

    /**
     * 资源类型
     */
    @TableField(value = "permission_type")
    private String permissionType;

    /**
     * 资源描述
     */
    @TableField(value = "permission_desc")
    private String permissionDesc;

    /**
     * 资源值
     */
    @TableField(value = "permission_value")
    private String permissionValue;

    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "state_flag")
    private String stateFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}