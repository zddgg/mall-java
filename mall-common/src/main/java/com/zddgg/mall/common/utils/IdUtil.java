package com.zddgg.mall.common.utils;

import java.util.UUID;

public class IdUtil {
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
