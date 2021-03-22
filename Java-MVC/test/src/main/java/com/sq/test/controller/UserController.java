package com.sq.test.controller;

import com.sq.annotation.Autowired;
import com.sq.annotation.Controller;
import com.sq.mvc.annotation.RequestMapping;
import com.sq.test.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public String test() {
        userService.helloWorld();
        return "success.jsp";
    }

}
