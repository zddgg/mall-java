package com.zddgg.mall.oms.vo.req;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OmsUserCreateReqVO {

    @NotBlank(message = "用户名不能为空!")
    private String username;

    private String password;

    private String email;

    private String mobile;

    @NotBlank(message = "角色编号不能为空!")
    private String roleId;
}
