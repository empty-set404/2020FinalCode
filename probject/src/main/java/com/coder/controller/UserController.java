package com.coder.controller;

import cn.coder.spring.bean.factory.annotation.Autowired;
import com.coder.vo.UserVo;
import com.coder.commont.Resp;
import com.coder.entity.User;
import com.coder.service.UserService;
import com.coder.springmvc.annotation.Controller;
import com.coder.springmvc.annotation.RequestMapping;
import com.coder.springmvc.annotation.RequestParam;
import com.coder.websocket.handler.WebSocketHandlerImpl;
import entity.HttpServletRequest;
import entity.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public Resp login(@RequestParam("username") String username, @RequestParam("password") String password) {
        // 参数校验
        if ("".equals(username) || "".equals(password)) {
            return Resp.error().data("data", "参数不为空");
        }

        UserVo userVo = userService.login(username, password);
        System.out.println("pppppppppp"+username+"  "+password);
        if (userVo == null) {
            return Resp.error().data("data", "用户名或密码错误");
        }

        // 添加用户
        User user = new User();
        user.setUsername(userVo.getUsername());
        user.setAvatar(userVo.getAvatar());
        WebSocketHandlerImpl.userMap.put(user, null);

        return Resp.ok().data("data", userVo);
    }

    @RequestMapping("/register")
    public Resp register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("avatar") String avatar) {
        // 参数校验
        if ("".equals(username) || "".equals(password) || "".equals(avatar)) {
            return Resp.error().data("data", "参数不为空");
        }

        int result = userService.register(username, password, avatar);
        if (result <= 0) {
            return Resp.error().data("data", "用户名已经存在");
        }
        return Resp.ok();
    }



    /**
     * 查询所有用户
     * @param response
     * @return
     */
    @RequestMapping("/findAll")
    public Resp findAll(HttpServletResponse response) {
        List<User> list = userService.findAll();

        return Resp.ok().data("data", list);
    }

    /**
     * 根据id 获取用户信息
     * @param id
     * @return
     */
    @RequestMapping("/getUser")
    public Resp getUserById(@RequestParam("id") String id) {
        User user = userService.getUserById(id);
        return Resp.ok().data("user", user);
    }

    /**
     * 更新用户信息
     * @param request
     * @param response
     * @param id
     * @param name
     * @throws IOException
     */
    @RequestMapping("/updateUser")
    public Resp updateUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id, @RequestParam("name") String name) throws IOException {
        int result = userService.updateUser(id, name);

        if (result >= 1) {
            return Resp.ok();
        }
        return Resp.error();
    }

    /**
     * 删除用户
     * @param response
     * @param id
     * @throws IOException
     */
    @RequestMapping("/deleteUser")
    public Resp deleteUser(HttpServletResponse response, @RequestParam("id") String id) throws IOException {
        int result = userService.deleteUser(id);

        if (result >= 1) {
            return Resp.ok();
        }
        return Resp.error();
    }

}

