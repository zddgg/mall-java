package com.zddgg.mall.product.bean.attr.req;

import lombok.Data;

import java.util.List;

@Data
public class AttrGroupCreateReqVo {

    private String groupId;

    private String groupName;

    private List<String> attrIds;
}
