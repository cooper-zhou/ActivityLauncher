package cn.cooper.support.activitylauncher.compiler.utils;

import com.squareup.javapoet.ClassName;

/**
 * Provide an easy way to get {@link com.squareup.javapoet.ClassName} for android class.
 */
public enum Android {

    Context("android.content.Context"),
    Intent("android.content.Intent"),

    Activity("android.app.Activity"),

    Bundle("android.os.Bundle");

    private ClassName className;

    Android(String qualifiedName) {
        className = ClassName.bestGuess(qualifiedName);
    }

    public ClassName className() {
        return className;
    }
}
