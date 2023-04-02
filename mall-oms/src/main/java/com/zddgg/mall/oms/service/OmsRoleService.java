package com.zddgg.mall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.oms.entity.OmsRole;

/**
* @author 86239
* @description 针对表【oms_role(OMS角色表)】的数据库操作Service
* @createDate 2023-04-01 17:17:33
*/
public interface OmsRoleService extends IService<OmsRole> {

    OmsRole getOneByRoleId(String roleId);
}
