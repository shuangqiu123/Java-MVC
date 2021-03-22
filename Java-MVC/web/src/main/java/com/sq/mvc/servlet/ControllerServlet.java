package com.sq.mvc.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.sq.annotation.Controller;
import com.sq.container.BeanContainer;
import com.sq.mvc.annotation.RequestMapping;
import com.sq.mvc.annotation.RequestParam;
import org.apache.commons.lang3.StringUtils;

/**
 * Controller servlet maps all the request except for static files into controller
 * load-on-startup = 1 will force it to load when the tomcat starts
 */
public class ControllerServlet extends HttpServlet {

    // map for request path to controller
    private Map<String, Object> controllersMapping = new HashMap<>();

    // map for request path to method information
    private Map<String, Method> handleMethodMapping = new HashMap<>();

    private ServletContext servletContext;

    // logger
    private Logger logger = Logger.getLogger(ControllerServlet.class.getName());


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        // mapping route
        String mappingPath = StringUtils.substringAfter(requestURI,
                StringUtils.replace(contextPath, "//", "/"));

        logger.info("Mapping request path: " + mappingPath);

        // get the mapping controller
        Object controller = controllersMapping.get(mappingPath);

        if (null != controller) {
            Method method = handleMethodMapping.get(mappingPath);

            if (method != null) {
                // invoke method
                try {
                    Parameter[] parameters = method.getParameters();
                    Object[] paramObjects = new Object[parameters.length];

                    // check for request param annotation
                    int i = 0;
                    for (Parameter parameter : parameters) {
                        if (parameter.isAnnotationPresent(RequestParam.class)) {
                            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                            String key = annotation.value();
                            String value = req.getParameter(key);

                            // Class<?> type = parameter.getType();
                            // TO-DO: cast values into declared type
                            paramObjects[i++] = value;
                        }
                        else if (parameter.getType() == HttpServletRequest.class) {
                            paramObjects[i++] = req;
                        }
                        else if (parameter.getType() == HttpServletResponse.class) {
                            paramObjects[i++] = resp;
                        }
                    }

                    // dispatch it to viewPath only
                    String viewPath = (String) method.invoke(controller, paramObjects);
                    if (!viewPath.startsWith("/")) {
                        viewPath = "/" + viewPath;
                    }
                    RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                    requestDispatcher.forward(req, resp);
                    return;

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        }

        // To-Do: RestController
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

         logger.info("Initialising controller servlet");

         this.servletContext = config.getServletContext();
         initHandleMethods();
    }

    /**
     *
     */
    private void initHandleMethods() {
        BeanContainer beanContainer = (BeanContainer) servletContext.getAttribute(BeanContainer.class.getName());

        // get the controller objects by the annotation controller
        Set<Object> controllers = beanContainer.getBeansByAnnotation(Controller.class);

        for (Object controller : controllers) {
            Class<?> controllerClass = controller.getClass();
            RequestMapping pathFromClass = controllerClass.getAnnotation(RequestMapping.class);
            String requestPath = pathFromClass.value();

            Method[] publicMethods = controllerClass.getMethods();
            // get all public methods and add the request mapping value
            for (Method method : publicMethods) {
                RequestMapping pathFromMethod = method.getAnnotation(RequestMapping.class);
                if (pathFromMethod != null) {
                    handleMethodMapping.put(requestPath + pathFromMethod.value(), method);
                    controllersMapping.put(requestPath + pathFromMethod.value(), controller);
                }
            }
        }
    }
}
