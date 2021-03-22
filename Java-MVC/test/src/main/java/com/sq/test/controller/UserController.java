package com.sq.test.controller;

import com.sq.annotation.Autowired;
import com.sq.annotation.Controller;
import com.sq.mvc.annotation.RequestMapping;
import com.sq.mvc.annotation.RequestParam;
import com.sq.test.domain.User;
import com.sq.test.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * testing controller
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(UserController.class.getName());

    @RequestMapping("/test")
    public String test() {
        logger.info("invoking test method");

        userService.helloWorld();
        return "success.jsp";
    }

    @RequestMapping("/register")
    public String register(@RequestParam("username") String username, HttpServletRequest req) {
        logger.info("invoking register user: " + username);
        User user = userService.registerUser(username);
        req.setAttribute("user", user);

        return "displayUser.jsp";
    }

    @RequestMapping("/id")
    public String getUserById(@RequestParam("id") String id, HttpServletRequest req) {
        logger.info("invoking get user by id: " + id);
        User user = userService.getUserById(Integer.parseInt(id));
        req.setAttribute("user", user);
        return "displayUser.jsp";
    }

}
