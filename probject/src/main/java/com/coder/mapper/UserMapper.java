package com.coder.mapper;

import cn.coder.spring.stereotype.Component;
import com.coder.entity.User;
import com.coder.mybatis.annotation.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user (username, password, avatar) values (?, ?, ?)")
    public int addUser(String username, String password, String avatar);

    @Select("select * from user where username = ? and password = ?")
    public User getUserByUsernameAndPassword(String username, String password);

    @Select("select * from user")
    public List<User> findAll();

    @Select("select * from user where id = #{id}")
    public User getUserById(String id);

    @Select("select * from user where username = ?")
    public User getUserByUsername(String username);

    @Update("update user set name = #{name} where id = #{id}")
    public int updateUser(String name, String id);

    @Delete("delete from user where id = #{id}")
    public int deleteUser(String id);

}
