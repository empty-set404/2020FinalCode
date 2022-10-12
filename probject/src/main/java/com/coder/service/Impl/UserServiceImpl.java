package com.coder.service.Impl;

import cn.coder.spring.bean.factory.annotation.Autowired;
import cn.coder.spring.stereotype.Component;
import cn.hutool.core.bean.BeanUtil;
import com.coder.RiemannRedisClient;
import com.coder.vo.UserVo;
import com.coder.entity.User;
import com.coder.mapper.UserMapper;
import com.coder.service.UserService;
import com.coder.utils.MD5Utils;

import java.lang.*;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RiemannRedisClient redisClient;

    @Override
    public UserVo login(String username, String password) {
        password = MD5Utils.md5(password);
        User user = userMapper.getUserByUsernameAndPassword(username, password);

        // 生成token, 这里使用 MD5( hashcode ^ 时间戳 )
        if (user != null) {
            UserVo userVo = new UserVo();
            String token = MD5Utils.md5(Long.toString(user.hashCode() + System.currentTimeMillis()));
            BeanUtil.copyProperties(user, userVo);
            userVo.setToken(token);
            // 存到redis中
            redisClient.set("token", token);

            return userVo;
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public int updateUser(String id, String name) {
        return userMapper.updateUser(name, id);
    }

    @Override
    public int deleteUser(String id) {
        return userMapper.deleteUser(id);
    }

    @Override
    public int register(String username, String password, String avatar) {
        User user = userMapper.getUserByUsername(username);
        if (user != null) {
            return -1;
        }
        return userMapper.addUser(username, MD5Utils.md5(password), avatar);
    }

}
