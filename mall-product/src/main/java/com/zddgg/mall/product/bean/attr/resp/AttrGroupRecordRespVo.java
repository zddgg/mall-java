package com.zddgg.mall.product.bean.attr.resp;

import lombok.Data;

import java.util.List;

@Data
public class AttrGroupRecordRespVo {

    private String groupId;

    private String groupName;

    private String status;

    private List<AttrUnitRecordRespVo> attrUnitRecords;
}
