package com.sq.mvc.annotation;
import java.lang.annotation.*;

/**
 * RequestMapping defines the path to a controller
 * Can be applied to both class and method
 * The actual path matching is the concatenation of both
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value() default "";
}
