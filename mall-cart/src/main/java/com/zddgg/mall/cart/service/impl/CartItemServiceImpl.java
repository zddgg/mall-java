package com.zddgg.mall.cart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.cart.entity.CartItem;
import com.zddgg.mall.cart.mapper.CartItemMapper;
import com.zddgg.mall.cart.service.CartItemService;
import org.springframework.stereotype.Service;

/**
* @author 86239
* @description 针对表【cart_item(购物车)】的数据库操作Service实现
* @createDate 2023-04-16 00:37:46
*/
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem>
    implements CartItemService {

}




