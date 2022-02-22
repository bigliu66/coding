package com.lx.mybatisplus.user.service.impl;

import com.lx.mybatisplus.user.entity.User;
import com.lx.mybatisplus.user.mapper.UserMapper;
import com.lx.mybatisplus.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-08-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
