package com.zddgg.mall.cart.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zddgg.mall.cart.bean.req.CartAddReqVO;
import com.zddgg.mall.cart.bean.req.CartSelectReqVO;
import com.zddgg.mall.cart.bean.req.SkuNumUpdateReqVO;
import com.zddgg.mall.cart.bean.resp.CartStoreInfoRespVO;
import com.zddgg.mall.cart.biz.CartBizService;
import com.zddgg.mall.cart.common.CartSelectedFlag;
import com.zddgg.mall.cart.common.StateFlag;
import com.zddgg.mall.cart.entity.CartItem;
import com.zddgg.mall.cart.exception.BizException;
import com.zddgg.mall.cart.service.CartItemService;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.IdUtil;
import com.zddgg.mall.product.api.response.SkuInfo;
import com.zddgg.mall.product.api.service.SkuQueryService;
import com.zddgg.mall.store.api.service.StoreQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartBizService cartBizService;

    private final CartItemService cartItemService;

    @DubboReference
    private SkuQueryService skuQueryService;

    @DubboReference
    private StoreQueryService storeQueryService;

    @PostMapping("getCartList")
    public Result<List<CartStoreInfoRespVO>> getCartList(HttpServletRequest request) {
        String userId = request.getHeader("user-id");
        return Result.success(cartBizService.getCartListByUserId(userId));
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

    @PostMapping("selectCart")
    public Result<List<CartStoreInfoRespVO>> selectCart(HttpServletRequest request,
                                                        @RequestBody CartSelectReqVO reqVO) {
        String userId = request.getHeader("user-id");
        if (StringUtils.isBlank(userId)) {
            throw new BizException("50008", "");
        }
        if (Arrays.asList("2", "3").contains(reqVO.getActionType())) {
            CartSelectedFlag selectedFlag;
            if (StringUtils.equals(reqVO.getActionType(), "2")) {
                selectedFlag = CartSelectedFlag.NOT_SELECTED;
            } else {
                selectedFlag = CartSelectedFlag.SELECTED;
            }
            cartItemService.update(
                    new LambdaUpdateWrapper<CartItem>()
                            .set(CartItem::getSelectedFlag, selectedFlag.getCode())
                            .eq(CartItem::getUserId, userId));
        } else {
            List<String> cartIds = new HashSet<>(reqVO.getCartIds()).stream().toList();
            if (!CollectionUtils.isEmpty(cartIds)
                    && Arrays.stream(CartSelectedFlag.values()).map(CartSelectedFlag::getCode)
                    .toList().contains(reqVO.getActionType())) {
                cartItemService.update(
                        new LambdaUpdateWrapper<CartItem>()
                                .set(CartItem::getSelectedFlag, reqVO.getActionType())
                                .in(CartItem::getCartId, cartIds)
                                .eq(CartItem::getUserId, userId));
            }
        }
        return Result.success(cartBizService.getCartListByUserId(userId));
    }

    @PostMapping("updateSkuNum")
    public Result<List<CartStoreInfoRespVO>> selectCart(HttpServletRequest request,
                                                        @RequestBody SkuNumUpdateReqVO reqVO) {
        String userId = request.getHeader("user-id");
        if (StringUtils.isBlank(userId)) {
            throw new BizException("50008", "");
        }

        String action = StringUtils.equals(reqVO.getActionType(), "0") ? "-" : "+";

        cartItemService.update(
                new LambdaUpdateWrapper<CartItem>()
                        .setSql("sku_num = sku_num " + action + " 1")
                        .eq(CartItem::getCartId, reqVO.getCartId())
                        .eq(CartItem::getUserId, userId));
        return Result.success(cartBizService.getCartListByUserId(userId));
    }
}
