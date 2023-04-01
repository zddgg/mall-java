package com.zddgg.mall.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.oms.entity.OmsRole;
import com.zddgg.mall.oms.mapper.OmsRoleMapper;
import com.zddgg.mall.oms.service.OmsRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 86239
 * @description 针对表【oms_role(OMS角色表)】的数据库操作Service实现
 * @createDate 2023-04-01 17:17:33
 */
@Service
public class OmsRoleServiceImpl extends ServiceImpl<OmsRoleMapper, OmsRole>
        implements OmsRoleService {

    @Override
    public OmsRole getOneByRoleId(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return null;
        }
        return this.getOne(
                new LambdaQueryWrapper<OmsRole>()
                        .eq(OmsRole::getRoleId, roleId)
        );
    }
}




