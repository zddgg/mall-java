package com.zddgg.mall.product.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.SkuDetailReqVo;
import com.zddgg.mall.product.bean.app.goods.SkuAttrOptionsRespVO;
import com.zddgg.mall.product.bean.app.goods.SkuFeedRespVO;
import com.zddgg.mall.product.entity.SkuAttrSale;
import com.zddgg.mall.product.entity.SkuMeta;
import com.zddgg.mall.product.entity.SpuAttrSale;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.SkuAttrSaleMapService;
import com.zddgg.mall.product.service.SkuMetaService;
import com.zddgg.mall.product.service.SpuAttrSaleMapService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("goods")
@RequiredArgsConstructor
public class GoodsController {

    private final SkuMetaService skuMetaService;

    private final SpuAttrSaleMapService spuAttrSaleMapService;

    private final SkuAttrSaleMapService skuAttrSaleMapService;

    @PostMapping("skuFeed")
    public Result<List<SkuFeedRespVO>> skuFeed(HttpServletRequest request) {
        String userId = request.getHeader("user-id");
        System.out.println(userId);
        List<SkuMeta> skuMetas = skuMetaService.list();

        List<SkuMeta> list = skuMetas.stream().collect(Collectors.groupingBy(SkuMeta::getSpuId))
                .values().stream().map(metas -> metas.get(0))
                .toList();

        List<SkuFeedRespVO> skuFeedRespVOS = list
                .stream()
                .map(skuMeta -> SkuFeedRespVO.builder()
                        .spuId(skuMeta.getSpuId())
                        .skuId(skuMeta.getSkuId())
                        .skuName(skuMeta.getSkuName())
                        .retailPrice(skuMeta.getRetailPrice().doubleValue())
                        .thumbnail(skuMeta.getThumbnail())
                        .build()).collect(Collectors.toList());
        return Result.success(skuFeedRespVOS);
    }

    @PostMapping("skuAttrOptions")
    public Result<SkuAttrOptionsRespVO> skuAttrOptions(@RequestBody SkuDetailReqVo reqVo) {
        SkuMeta skuMeta = skuMetaService.getOne(
                new LambdaQueryWrapper<SkuMeta>()
                        .eq(SkuMeta::getSkuId, reqVo.getSkuId())
        );
        if (Objects.isNull(skuMeta)) {
            throw new BizException("商品不存在！");
        }
        List<SpuAttrSale> spuAttrSales = spuAttrSaleMapService.getListBySpuId(skuMeta.getSpuId());
        List<SkuAttrSale> skuAttrSales = skuAttrSaleMapService.getListBySpuId(skuMeta.getSpuId());

        Map<String, SpuAttrSale> attrId2SpuAttrMap = spuAttrSales.stream().collect(Collectors.toMap(SpuAttrSale::getAttrId, v -> v));

        Map<String, String> attrValueId2AttrValueName = skuAttrSales.stream().collect(Collectors.toMap(SkuAttrSale::getAttrValueId, SkuAttrSale::getAttrValueName, (v1, v2) -> v2));

        Map<String, Set<String>> attrIdMap2attrValueIdSet = skuAttrSales.stream()
                .collect(Collectors.groupingBy(SkuAttrSale::getAttrId, Collectors.mapping(SkuAttrSale::getAttrValueId, Collectors.toSet())));

        Map<String, Set<String>> skuIdMap2attrValueIdSet = skuAttrSales.stream()
                .collect(Collectors.groupingBy(SkuAttrSale::getSkuId, Collectors.mapping(SkuAttrSale::getAttrValueId, Collectors.toSet())));

        Map<String, Map<String, String>> skuId2AttrId2AttrValueId = skuAttrSales.stream()
                .collect(Collectors.groupingBy(SkuAttrSale::getSkuId,
                        Collectors.toMap(SkuAttrSale::getAttrId, SkuAttrSale::getAttrValueId)));

        List<SkuAttrOptionsRespVO.SkuAttrOption> skuAttrOptions = attrIdMap2attrValueIdSet.entrySet().stream()
                .map(entry -> {
                            return SkuAttrOptionsRespVO.SkuAttrOption
                                    .builder()
                                    .id(attrId2SpuAttrMap.get(entry.getKey()).getAttrId())
                                    .name(attrId2SpuAttrMap.get(entry.getKey()).getAttrName())
                                    .list(entry.getValue()
                                            .stream()
                                            .map(attrValueId -> {
                                                        // 当前商品其他属性组选中的属性
                                                        Map<String, String> attrId2AttrValueIdMap = skuId2AttrId2AttrValueId.get(reqVo.getSkuId()).entrySet().stream()
                                                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                                                        boolean skuExist = false;
                                                        if (StringUtils.equals(attrId2AttrValueIdMap.get(entry.getKey()), attrValueId)) {
                                                            skuExist = true;
                                                        } else {
                                                            attrId2AttrValueIdMap.put(entry.getKey(), attrValueId);
                                                            for (Map.Entry<String, Map<String, String>> mapEntry : skuId2AttrId2AttrValueId.entrySet()) {
                                                                skuExist = mapEntry.getValue().entrySet()
                                                                        .stream()
                                                                        .allMatch(attrMapEntry ->
                                                                                StringUtils.equals(attrId2AttrValueIdMap.get(attrMapEntry.getKey()), attrMapEntry.getValue()));
                                                                if (skuExist) {
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        return SkuAttrOptionsRespVO.SkuAttr
                                                                .builder()
                                                                .id(attrValueId)
                                                                .name(attrValueId2AttrValueName.get(attrValueId))
                                                                .active(skuIdMap2attrValueIdSet.get(reqVo.getSkuId()).contains(attrValueId))
                                                                .disable(!skuExist)
                                                                .build();
                                                    }
                                            ).toList())
                                    .build();
                        }
                ).toList();

        SkuAttrOptionsRespVO.Goods goods = SkuAttrOptionsRespVO.Goods
                .builder()
                .skuId(skuMeta.getSkuId())
                .price(skuMeta.getRetailPrice().toString())
                .imagePath(skuMeta.getThumbnail())
                .build();

        List<SkuAttrOptionsRespVO.SkuAttrMap> skuAttrMaps = skuAttrSales.stream()
                .collect(Collectors.groupingBy(SkuAttrSale::getSkuId))
                .entrySet()
                .stream()
                .map(entry -> SkuAttrOptionsRespVO.SkuAttrMap
                        .builder()
                        .skuId(entry.getKey())
                        .attrMap(entry.getValue()
                                .stream()
                                .collect(Collectors.toMap(SkuAttrSale::getAttrId, SkuAttrSale::getAttrValueId))
                        ).build()).toList();
        return Result.success(SkuAttrOptionsRespVO
                .builder()
                .skuAttrOptions(skuAttrOptions)
                .goods(goods)
                .skuAttrMaps(skuAttrMaps)
                .build());
    }
}
