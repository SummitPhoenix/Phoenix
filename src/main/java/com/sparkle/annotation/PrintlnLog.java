package com.sparkle.annotation;

import java.lang.annotation.*;

/**
 * @author Smartisan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface PrintlnLog {
    /**
     * 自定义日志描述信息文案
     */
    String description() default "";
}
