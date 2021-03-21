package container;

public class BeanContainer {
    private static BeanContainer beanContainer;

    private BeanContainer(){};

    /**
     * Singleton class
     * @return global instance of bean container
     */
    public static BeanContainer getInstance() {
        if (beanContainer == null) {
            beanContainer = new BeanContainer();
        }
        return beanContainer;
    }



}
