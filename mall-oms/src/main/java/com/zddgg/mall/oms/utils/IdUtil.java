package com.zddgg.mall.oms.utils;

import java.util.UUID;

public class IdUtil {
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
