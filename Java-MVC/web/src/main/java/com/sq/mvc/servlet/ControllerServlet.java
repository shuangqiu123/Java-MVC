package com.sq.mvc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import com.sq.annotation.Controller;
import com.sq.container.BeanContainer;
import org.apache.commons.lang3.StringUtils;

/**
 * Controller servlet maps all the request except for static files into controller
 */
public class ControllerServlet extends HttpServlet {

    private ControllerHandler controllerHandler = new ControllerHandler();
    private ServletContext servletContext;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        // mapping route
        String mappingPath = StringUtils.substringAfter(requestURI,contextPath);


    }

    @Override
    public void init(ServletConfig config) throws ServletException {
         this.servletContext = config.getServletContext();
         initHandleMethods();
    }

    private void initHandleMethods() {
        BeanContainer beanContainer = (BeanContainer) servletContext.getAttribute("BeanContainer");

        // get the controller objects by the annotation controller
        Set<Object> controllers = beanContainer.getBeansByAnnotation(Controller.class);


    }
}
