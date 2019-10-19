package cn.cooper.support.activitylauncher.compiler.model;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import cn.cooper.support.activitylauncher.annotation.Launcher;
import cn.cooper.support.activitylauncher.compiler.utils.Android;

/**
 * Contains a class's elements
 */
public class LauncherModel {

    private String packageName;
    private String simpleName;
    private String qualifiedName;

    private int[] flags;
    private int pendingTransition;
    private int pendingTransitionOnFinish;

    public LauncherModel(PackageElement packageElement, TypeElement typeElement) {
        this.packageName = packageElement.getQualifiedName().toString();
        this.simpleName = typeElement.getSimpleName().toString();
        this.qualifiedName = typeElement.getQualifiedName().toString();
        Launcher launcher = typeElement.getAnnotation(Launcher.class);
        this.flags = launcher.flags();
        this.pendingTransition = launcher.pendingTransition();
        this.pendingTransitionOnFinish = launcher.pendingTransitionOnFinish();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public int[] getFlags() {
        return flags;
    }

    public int getPendingTransition() {
        return pendingTransition;
    }

    public int getPendingTransitionOnFinish() {
        return pendingTransitionOnFinish;
    }

    public Android getLaunchContext() {
        return pendingTransition != 0 && pendingTransitionOnFinish != 0 ? Android.Activity : Android.Context;
    }
}
