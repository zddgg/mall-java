package com.zddgg.mall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.cart.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 86239
* @description 针对表【cart_item(购物车)】的数据库操作Mapper
* @createDate 2023-04-16 00:37:46
* @Entity generator.entity.CartItem
*/
@Mapper
@Repository
public interface CartItemMapper extends BaseMapper<CartItem> {

}




