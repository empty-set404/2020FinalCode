package com.coder.service;

import com.coder.vo.UserVo;
import com.coder.entity.User;

import java.util.List;


public interface UserService {

    UserVo login(String username, String password);

    List<User> findAll();

    User getUserById(String id);

    User getUserByUsername(String username);

    int updateUser(String id, String name);

    int deleteUser(String id);

    int register(String username, String password, String avatar);
}
