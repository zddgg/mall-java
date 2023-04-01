package com.zddgg.mall.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * OMS角色资源表
 *
 * @TableName oms_role_permission
 */
@TableName(value = "oms_role_permission")
@Getter
@Setter
public class OmsRolePermission extends BaseEntity implements Serializable {

    /**
     * 角色编号
     */
    @TableField(value = "role_id")
    private String roleId;

    /**
     * 资源编号
     */
    @TableField(value = "permission_id")
    private String permissionId;

    /**
     * 状态: 0-停用, 1-启用, -1-删除
     */
    @TableField(value = "state_flag")
    private String stateFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}