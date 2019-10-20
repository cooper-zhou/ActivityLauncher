package cn.cooper.support.activitylauncher.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To generate a launcher class for activity when compiling.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented
@Inherited
public @interface Launcher {

    int[] flags() default {};

    int pendingTransition() default 0;

    int pendingTransitionOnFinish() default 0;
}
