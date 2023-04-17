package com.zddgg.mall.cart.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.cart.bean.resp.CartStoreInfoRespVO;
import com.zddgg.mall.cart.biz.CartBizService;
import com.zddgg.mall.cart.common.CartSelectedFlag;
import com.zddgg.mall.cart.entity.CartItem;
import com.zddgg.mall.cart.service.CartItemService;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.IdUtil;
import com.zddgg.mall.product.api.response.SkuInfo;
import com.zddgg.mall.product.api.service.SkuQueryService;
import com.zddgg.mall.store.api.response.StoreInfo;
import com.zddgg.mall.store.api.service.StoreQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartBizServiceImpl implements CartBizService {

    private final CartItemService cartItemService;

    @DubboReference
    private SkuQueryService skuQueryService;

    @DubboReference
    private StoreQueryService storeQueryService;

    @Override
    public List<CartStoreInfoRespVO> getCartListByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return new ArrayList<>();
        }
        List<CartItem> cartItems = cartItemService.list(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getUserId, userId));

        Map<String, List<CartItem>> storeId2Cart = cartItems.stream()
                .collect(Collectors.groupingBy(CartItem::getStoreId));

        Map<String, StoreInfo> storeId2StoreInfopMap = storeQueryService.batchQuery(storeId2Cart.keySet().stream().toList())
                .stream()
                .collect(Collectors.toMap(StoreInfo::getStoreId, v -> v));

        Map<String, SkuInfo> skuId2SkuInfoMap = skuQueryService.batchQuery(cartItems.stream().map(CartItem::getSkuId).toList())
                .stream()
                .collect(Collectors.toMap(SkuInfo::getSkuId, v -> v));

        return storeId2Cart.entrySet()
                .stream()
                .map(entry -> {
                    List<CartStoreInfoRespVO.CartSkuInfo> cartSkuInfoList = entry.getValue()
                            .stream()
                            .map(cartItem -> {
                                SkuInfo skuInfo = skuId2SkuInfoMap.get(cartItem.getSkuId());
                                return CartStoreInfoRespVO.CartSkuInfo
                                        .builder()
                                        .cartInfo(
                                                CartStoreInfoRespVO.CartInfo
                                                        .builder()
                                                        .cartId(cartItem.getCartId())
                                                        .storeId(cartItem.getStoreId())
                                                        .skuId(cartItem.getSkuId())
                                                        .skuNum(cartItem.getSkuNum())
                                                        .addPrice(cartItem.getAddPrice())
                                                        .selected(StringUtils.equals(CartSelectedFlag.SELECTED.getCode(), cartItem.getSelectedFlag()))
                                                        .build()
                                        )
                                        .skuInfo(
                                                CartStoreInfoRespVO.SkuInfo
                                                        .builder()
                                                        .skuId(skuInfo.getSkuId())
                                                        .skuName(skuInfo.getSkuName())
                                                        .thumbnail(skuInfo.getThumbnail())
                                                        .attrStr("红色，128GB")
                                                        .retailPrice(skuInfo.getRetailPrice())
                                                        .build()
                                        )
                                        .build();
                            }).toList();
                    CartStoreInfoRespVO.CartPreferentialInfo cartPreferentialInfo = new CartStoreInfoRespVO.CartPreferentialInfo();
                    cartPreferentialInfo.setPreferentialId(IdUtil.getId());
                    cartPreferentialInfo.setPreferentialTitle("已满699元，已减200元");
                    cartPreferentialInfo.setCartSkuInfos(cartSkuInfoList);
                    CartStoreInfoRespVO.CartPreferentialInfo cartPreferentialInfo1 = new CartStoreInfoRespVO.CartPreferentialInfo();
                    cartPreferentialInfo1.setPreferentialId(IdUtil.getId());
                    cartPreferentialInfo1.setPreferentialTitle("已满1099元，已减500元");
                    cartPreferentialInfo1.setCartSkuInfos(cartSkuInfoList);
                    return CartStoreInfoRespVO
                            .builder()
                            .storeId(entry.getKey())
                            .storeName((storeId2StoreInfopMap.get(entry.getKey()).getStoreName()))
                            .cartPreferentialInfos(Arrays.asList(cartPreferentialInfo, cartPreferentialInfo1))
                            .build();
                }).toList();

    }
}
