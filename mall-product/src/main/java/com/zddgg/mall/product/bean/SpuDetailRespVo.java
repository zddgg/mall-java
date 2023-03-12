package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.SpuAttrSaleMap;
import com.zddgg.mall.product.entity.SpuMeta;
import lombok.Data;

import java.util.List;

@Data
public class SpuDetailRespVo {

    private SpuMeta spuMeta;

    private List<SpuAttrSaleMap> spuAttrSaleMaps;
}
