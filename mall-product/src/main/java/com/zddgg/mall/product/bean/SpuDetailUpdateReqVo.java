package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.SpuMeta;
import lombok.Data;

import java.util.List;

@Data
public class SpuDetailUpdateReqVo {

    private SpuMeta spuMeta;

    private List<String> deleteAttrIds;

    private List<String> addAttrNames;
}
