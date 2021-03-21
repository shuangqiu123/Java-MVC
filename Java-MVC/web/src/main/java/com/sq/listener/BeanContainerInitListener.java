package com.sq.listener;

import com.sq.container.BeanContainer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener that creates bean container after initialising servlet context
 */
public class BeanContainerInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.init(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.destroy();
    }
}
