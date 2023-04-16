package com.zddgg.mall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.user.entity.UserInfo;
import com.zddgg.mall.user.mapper.UserInfoMapper;
import com.zddgg.mall.user.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
* @author 86239
* @description 针对表【user_info(基础信息表)】的数据库操作Service实现
* @createDate 2023-04-05 21:02:34
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {

}




