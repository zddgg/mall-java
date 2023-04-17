package com.zddgg.mall.cart.biz;

import com.zddgg.mall.cart.bean.resp.CartStoreInfoRespVO;

import java.util.List;

public interface CartBizService {

    List<CartStoreInfoRespVO> getCartListByUserId(String userId);
}
