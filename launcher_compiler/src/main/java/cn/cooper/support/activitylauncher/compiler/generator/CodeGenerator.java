package cn.cooper.support.activitylauncher.compiler.generator;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ExecutableElement;
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

    boolean putActivityResultElement(ExecutableElement element);
}
