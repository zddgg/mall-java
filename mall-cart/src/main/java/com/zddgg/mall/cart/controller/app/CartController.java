package com.zddgg.mall.cart.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.cart.bean.req.CartAddReqVO;
import com.zddgg.mall.cart.bean.resp.CartStoreInfoRespVO;
import com.zddgg.mall.cart.common.CartSelectedFlag;
import com.zddgg.mall.cart.common.StateFlag;
import com.zddgg.mall.cart.entity.CartItem;
import com.zddgg.mall.cart.exception.BizException;
import com.zddgg.mall.cart.service.CartItemService;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.IdUtil;
import com.zddgg.mall.product.api.response.SkuInfo;
import com.zddgg.mall.product.api.service.SkuQueryService;
import com.zddgg.mall.store.api.response.StoreInfo;
import com.zddgg.mall.store.api.service.StoreQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    @DubboReference
    private SkuQueryService skuQueryService;

    @DubboReference
    private StoreQueryService storeQueryService;

    @PostMapping("getCartList")
    public Result<List<CartStoreInfoRespVO>> getCartList(HttpServletRequest request) {
        String userId = request.getHeader("user-id");
        if (StringUtils.isBlank(userId)) {
            return Result.success();
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

        List<CartStoreInfoRespVO> respVOList = storeId2Cart.entrySet()
                .stream()
                .map(entry -> {
                    CartStoreInfoRespVO.CartPreferentialInfo cartPreferentialInfo = new CartStoreInfoRespVO.CartPreferentialInfo();
                    cartPreferentialInfo.setPreferentialTitle("已满1099元，已减200元");
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
                    cartPreferentialInfo.setCartSkuInfos(cartSkuInfoList);
                    return CartStoreInfoRespVO
                            .builder()
                            .storeId(entry.getKey())
                            .storeName((storeId2StoreInfopMap.get(entry.getKey()).getStoreName()))
                            .cartPreferentialInfos(Collections.singletonList(cartPreferentialInfo))
                            .build();
                }).toList();

        return Result.success(respVOList);
    }

    @PostMapping("addCart")
    public Result<Object> addCart(HttpServletRequest request, @RequestBody CartAddReqVO reqVO) {
        String userId = request.getHeader("user-id");
        if (StringUtils.isBlank(userId)) {
            throw new BizException("50008", "");
        }

        if (StringUtils.isBlank(reqVO.getSkuId())) {
            return Result.fail();
        }
        SkuInfo skuInfo = skuQueryService.queryBySkuId(reqVO.getSkuId());
        if (Objects.isNull(skuInfo)) {
            return Result.fail();
        }
        Integer skuNum = Optional.ofNullable(reqVO.getSkuNum()).orElse(1);

        CartItem cartItem = cartItemService.getOne(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getUserId, userId)
                        .eq(CartItem::getSkuId, reqVO.getSkuId()));

        if (Objects.nonNull(cartItem)) {
            cartItem.setSkuNum(cartItem.getSkuNum() + skuNum);
            cartItem.setSelectedFlag(CartSelectedFlag.SELECTED.getCode());
            cartItemService.updateById(cartItem);
            return Result.success();
        }

        cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setCartId(IdUtil.getId());
        cartItem.setStoreId(skuInfo.getStoreId());
        cartItem.setSkuId(skuInfo.getSkuId());
        cartItem.setSkuNum(skuNum);
        cartItem.setAddPrice(skuInfo.getRetailPrice());
        cartItem.setSelectedFlag(CartSelectedFlag.SELECTED.getCode());
        cartItem.setStateFlag(StateFlag.ENABLED.code);
        cartItemService.save(cartItem);
        return Result.success();
    }
}
