package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.voidtime.mall.product.entity.BackendCategory;
import com.voidtime.mall.product.mapper.BackendCategoryMapper;
import com.voidtime.mall.product.service.BackendCategoryService;
import org.springframework.stereotype.Service;

/**
* @author 86239
* @description 针对表【backend_category(后台类目表)】的数据库操作Service实现
* @createDate 2022-11-30 17:38:36
*/
@Service
public class BackendCategoryServiceImpl extends ServiceImpl<BackendCategoryMapper, BackendCategory>
    implements BackendCategoryService{
}




