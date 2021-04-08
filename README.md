# Java-MVC

A MVC framework which uses Inversion of Control and has achieved some functionality of Spring framework.

## Supported Annotations

```java
@Service, @Component, @Controller // for marking class as beans
@Autowired // supports field level injection of beans
@RequestParam, @RequestMapping // for MVC controller
```

## Key Classes

### BeanContainer

- Created when ServletContext Initialised (invoked by the ServletContextListener)

- Use ClassLoader to load relevant packages and instantiate beans marked by annotations
- Search @Autowired fields in beans and inject the corresponding bean object
- Search @PostConstruct methods in beans and invoke those methods

### DispatcherServlet

- Created at start (load-on-startup == 1) and maps all request path to this servlet
- Created mapping between request path and the controller class, as well as mapping between request path and method
- Invoke the specific method by searching in the hash map using the request path 

