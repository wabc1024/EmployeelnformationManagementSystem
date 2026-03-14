package com.wabc.service;

import com.wabc.pojo.User;

public interface UserService {
    User queryUserByUserName(String username);

    int addUser(User user);

    int updateUser(User user);

    int deleteUserById(Integer userId);
}
