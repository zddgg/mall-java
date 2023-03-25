package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.SpuAttrSale;
import com.zddgg.mall.product.entity.SpuMeta;
import lombok.Data;

import java.util.List;

@Data
public class SpuDetailRespVo {

    private SpuMeta spuMeta;

    private List<SpuAttrSale> spuAttrSales;
}
