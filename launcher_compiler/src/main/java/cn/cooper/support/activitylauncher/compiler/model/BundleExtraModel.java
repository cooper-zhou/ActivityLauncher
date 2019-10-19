package cn.cooper.support.activitylauncher.compiler.model;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class BundleExtraModel {

    private String name;
    private BundleExtraType extraType;
    private TypeName type;
    private TypeName rawType;

    public BundleExtraModel(VariableElement variableElement) {
        TypeMirror typeMirror = variableElement.asType();
        this.name = variableElement.getSimpleName().toString();
        this.extraType = BundleExtraType.get(variableElement);
        this.type = this.extraType.getTypeName(typeMirror);
        this.rawType = BundleExtraType.getRawType(typeMirror);
    }

    public String getName() {
        return name;
    }

    public BundleExtraType getExtraType() {
        return extraType;
    }

    public TypeName getType() {
        return type;
    }

    public TypeName getRawType() {
        return rawType;
    }
}
