package com.sq.container;

import com.sq.annotation.Autowired;
import com.sq.annotation.Component;
import com.sq.annotation.Controller;
import com.sq.annotation.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanContainer {
    public static final String BEAN_CONTAINER_NAME = BeanContainer.class.getName();

    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS
            = Arrays.asList(Component.class, Controller.class, Service.class);

    private ClassLoader classLoader;

    // the container for all the beans
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    // the servlet context
    private static ServletContext servletContext;


    /**
     * Singleton class
     * @return global instance of bean container
     */
    public static BeanContainer getInstance() {
        return (BeanContainer) servletContext.getAttribute(BEAN_CONTAINER_NAME);
    }

    /**
     * Initialization of Bean Container
     * @param servletContext
     * @throws RuntimeException
     */
    public void init(ServletContext servletContext) throws RuntimeException {
        BeanContainer.servletContext = servletContext;
        servletContext.setAttribute(BEAN_CONTAINER_NAME, this);

        // get ServletContext ClassLoader
        this.classLoader = servletContext.getClassLoader();

        this.instantiateBeans();
        this.initializeBeans();
    }

    /**
     * find search for autowired attributes and inject the dependency
     */
    private void initializeBeans() {
        beanMap.values().forEach(bean -> inject(bean));
    }

    // dependency injection
    private void inject(Object bean) {
        Class<?> beanClass = bean.getClass();

        Stream.of(beanClass.getDeclaredFields())
        // filter field by non static and has Autowired present
        .filter(field -> {
            int mods = field.getModifiers();
            return !Modifier.isStatic(mods) && field.isAnnotationPresent(Autowired.class);
        })
        .forEach(field -> {
            Class<?> declaringClass = field.getType();
            Object o = beanMap.get(declaringClass);
            // inject bean
            try {
                field.setAccessible(true);
                field.set(bean, o);
            } catch (IllegalAccessException e) {
                e.getCause().printStackTrace();
            }
        });
    }

    /**
     * use reflection to instantiate an instance of a class
     * @param clz
     * @return
     */
    private Object createInstance(Class<?> clz) {
        Object o = null;
        try {
            o = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    private Set<Class<?>> loadClasses(String basePackage) {
        URL url =classLoader.getResource(basePackage.replace(".", "/"));
        if (null == url) {
            throw new RuntimeException("cannot get resource");
        }
        try {
            if (url.getProtocol().equalsIgnoreCase("file")) {
                File file = new File(url.getFile());
                Path basePath = file.toPath();
                return Files.walk(basePath)
                        .filter(path -> path.toFile().getName().endsWith(".class"))
                        .map(path -> {
                            String pathString = path.toString();
                            String packageName = pathString.replace(basePath.toString(),"");
                            String className = (basePackage + packageName)
                                    .replace("/", ".")
                                    .replace("\\", ".")
                                    .replace(".class", "");
                            Class<?> clz = null;
                            try {
                                clz = classLoader.loadClass(className);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            return clz;
                        })
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Search for beans and put then into beanMap
     */
    private void instantiateBeans() {
        // load the class
        Set<Class<?>> classes = this.loadClasses("com.sq.test");

        //nd classes with annotation in BEAN_ANNOTATIONS and instantiate them fi
        classes.stream().filter(clz -> {
            for (Class<? extends Annotation> annotation: BEAN_ANNOTATIONS) {
                if (clz.isAnnotationPresent(annotation)) {
                    return true;
                }
            }
            return false;
        }).forEach(clz -> {
            beanMap.put(clz, createInstance(clz));
        });
    }

    public Object getBean(Class<?> beanClass) {
        return beanMap.get(beanClass);
    }

    public Set<Object> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return beanMap
                .keySet()
                .stream()
                .filter(bean -> bean.isAnnotationPresent(annotation))
                .map(bean -> beanMap.get(bean))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public int beanMapSize() {
        return beanMap.size();
    }

    public void destroy() {

    }
}
