package com.zddgg.mall.product.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class RegionMeta {

    @ExcelProperty("编码")
    private String code;

    @ExcelProperty("名称")
    private String name;
}
