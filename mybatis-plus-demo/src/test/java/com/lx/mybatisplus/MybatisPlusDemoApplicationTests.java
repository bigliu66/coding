package com.lx.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lx.mybatisplus.user.entity.User;
import com.lx.mybatisplus.user.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusDemoApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void contextLoads() {
		System.out.println(("----- selectAll method test ------"));
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("name", "Tom").eq("id", 3);
		//通用Mapper
		List<User> userList = userMapper.selectList(queryWrapper);
		userList.forEach(System.out::println);
		//自定义Mapper
//		List<User> userList = userMapper.selectOrderByAge();
//		userList.forEach(System.out::println);
		//通用Mapper分页设置当前页和页容量
		Page<User> page = new Page<>(1, 2);
		IPage<User> userIPage = userMapper.selectPage(page,queryWrapper);
		System.out.println("总页数："+userIPage.getPages());
		System.out.println("总记录数："+userIPage.getTotal());
		userIPage.getRecords().forEach(System.out::println);
		//自定义Mapper分页设置当前页和页容量
//		Page<User> page = new Page<>(1, 2, false);
//		IPage<User> userIPage = userMapper.selectOrderByAge(page);
//		如果设置page第三个参数为false则不统计总记录数，故用下面的方法获取不到总页数和总记录数
//		System.out.println("总页数："+userIPage.getPages());
//		System.out.println("总记录数："+userIPage.getTotal());
//		userIPage.getRecords().forEach(System.out::println);
	}

}

