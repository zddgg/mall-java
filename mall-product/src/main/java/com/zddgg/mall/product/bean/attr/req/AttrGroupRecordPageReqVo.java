package com.zddgg.mall.product.bean.attr.req;

import com.zddgg.mall.common.request.PaginationReq;
import lombok.Data;

@Data
public class AttrGroupRecordPageReqVo extends PaginationReq {

    private String groupId;

    private String groupName;

}
