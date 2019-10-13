package com.kyle.support.activitystarter.compiler.model;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Contains a class's elements
 */
public class ClassModel {

    private String packageName;
    private String simpleName;
    private String qualifiedName;

    public ClassModel(PackageElement packageElement, TypeElement typeElement) {
        this.packageName = packageElement.getQualifiedName().toString();
        this.simpleName = typeElement.getSimpleName().toString();
        this.qualifiedName = typeElement.getQualifiedName().toString();
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
}
