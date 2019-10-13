package com.kyle.support.activitystarter.compiler.generator;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.VariableElement;

/**
 * Define interfaces for code generating.
 */
public interface CodeGenerator {

    String getSimpleName();

    String getQualifiedName();

    String getPackageName();

    TypeSpec generateCode();

    boolean putRequiredElement(VariableElement element);

    boolean putOptionalElement(VariableElement element);
}