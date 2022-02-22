package com.lx.mybatisplus.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lx.mybatisplus.user.entity.User;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2020-08-19
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> selectOrderByAge();

    IPage<User> selectOrderByAge(IPage<User> page);

}
