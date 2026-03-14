package com.wabc.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wabc.mapper.UserMapper;
import com.wabc.pojo.User;
import com.wabc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  //开启事物
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",username);
        queryWrapper.ne("is_deleted",1);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public int deleteUserById(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setIsDeleted(true);
        return userMapper.updateById(user);
    }
}
