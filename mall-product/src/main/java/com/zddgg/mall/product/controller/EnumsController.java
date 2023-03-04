package com.zddgg.mall.product.controller;

import com.voidtime.mall.common.response.Result;
import com.voidtime.mall.product.bean.EnumReqVo;
import com.voidtime.mall.product.bean.SelectOptionData;
import com.voidtime.mall.product.constant.property.PropertyEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("enum")
public class EnumsController {

    @PostMapping("query")
    public Result<List<SelectOptionData>> queryEnum(@RequestBody EnumReqVo reqVo) {
        List<SelectOptionData> data = new ArrayList<>();
        if (StringUtils.equals(reqVo.getModuleName(), "Property")) {
            if (StringUtils.equals(reqVo.getEnumName(), "FormShowType")) {
                data = Arrays.stream(PropertyEnums.FormShowType.values())
                        .map(formShowType -> {
                            SelectOptionData selectOptionData = new SelectOptionData();
                            selectOptionData.setValue(formShowType.code);
                            selectOptionData.setLabel(formShowType.msg);
                            return selectOptionData;
                        }).collect(Collectors.toList());
            }
        }
        return Result.success(data);
    }
}
