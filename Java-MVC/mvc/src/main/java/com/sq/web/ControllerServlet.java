package com.sq.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
/**
 * Controller servlet maps all the request except for static files into controller
 */
public class ControllerServlet extends HttpServlet {

    private ControllerHandler controllerHandler = new ControllerHandler();


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        // mapping route
        String mappingPath = StringUtils.substringAfter(requestURI,contextPath);


    }
}
