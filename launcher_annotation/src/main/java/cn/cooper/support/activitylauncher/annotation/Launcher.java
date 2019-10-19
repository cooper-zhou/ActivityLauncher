package cn.cooper.support.activitylauncher.annotation;

/**
 * To generate a starter class for activity when compiling.
 */
public @interface Launcher {

    int[] flags() default {};

    int pendingTransition() default 0;

    int pendingTransitionOnFinish() default 0;
}
