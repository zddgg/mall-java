package com.zddgg.mall.product.bean.app.goods;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Accessors(chain = true)
public class SkuAttrOptionsRespVO {

    private List<SkuAttrOption> skuAttrOptions;

    private Goods goods;

    private List<SkuAttrMap> skuAttrMaps;

    @Data
    @Builder
    @Accessors(chain = true)
    public static class Goods {
        private String skuId;
        private String price;
        private String imagePath;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    public static class SkuAttrOption {
        private String id;

        private String name;

        private List<SkuAttr> list;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    public static class  SkuAttr {
        private String id;

        private String name;

        private Boolean active;

        private Boolean disable;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    public static class SkuAttrMap {

        private String skuId;

        private Map<String, String> attrMap;
    }
}
