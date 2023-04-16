package com.zddgg.mall.file.bean.req;

import lombok.Data;

@Data
public class FileQueryReqVO {

    private String accessRule;

    private String fileType;

    private String fileId;
}
