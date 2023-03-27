package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.bean.attr.resp.AttrGroupRecordRespVo;
import com.zddgg.mall.product.bean.attr.resp.AttrSaleRecordRespVo;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryDetail {
    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;

    private Boolean relatedAttr;

    private List<AttrUnitRecordRespVo> attrUnitRecords;

    private List<AttrGroupRecordRespVo> attrGroupRecords;

    private List<AttrSaleRecordRespVo> attrSaleRecords;
}