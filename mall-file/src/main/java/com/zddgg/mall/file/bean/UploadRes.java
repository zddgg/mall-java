package com.zddgg.mall.file.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadRes {

    private String uid;

    private String name;

    private String url;
}
