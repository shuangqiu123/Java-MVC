package com.sq.listener;

import com.sq.container.BeanContainer;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * Listener that creates bean container after initialising servlet context
 */
public class BeanContainerInitListener implements ServletContextListener {
    private Logger logger = Logger.getLogger(BeanContainerInitListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BeanContainer beanContainer = new BeanContainer();

        logger.info("start initialising bean container");

        beanContainer.init(servletContextEvent.getServletContext());

        logger.info("current bean size: " + beanContainer.beanMapSize());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.destroy();
    }
}
