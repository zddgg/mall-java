package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.PropertySaleValue;
import com.zddgg.mall.product.mapper.PropertySaleValueMapper;
import com.zddgg.mall.product.service.PropertySaleValueService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_sale_value(销售属性value表)】的数据库操作Service实现
 * @createDate 2023-03-05 09:06:08
 */
@Service
public class PropertySaleValueServiceImpl extends ServiceImpl<PropertySaleValueMapper, PropertySaleValue>
        implements PropertySaleValueService {

    @Override
    public List<PropertySaleValue> getListByAttrIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return this.list(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .in(PropertySaleValue::getKeyId, ids)
        );
    }
}




