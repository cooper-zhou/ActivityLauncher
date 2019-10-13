package com.kyle.support.activitystarter.compiler.generator;

import com.kyle.support.activitystarter.compiler.model.ClassModel;
import com.kyle.support.activitystarter.compiler.model.BundleExtraModel;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public abstract class BaseCodeGenerator implements CodeGenerator {

    private ClassModel mTargetClassModel;

    List<BundleExtraModel> mRequiredExtras = new ArrayList<>();
    List<BundleExtraModel> mOptionalExtras = new ArrayList<>();

    BaseCodeGenerator(PackageElement packageElement, TypeElement typeElement) {
        mTargetClassModel = new ClassModel(packageElement, typeElement);
    }

    @Override
    public boolean putRequiredElement(VariableElement element) {
        return mRequiredExtras.add(new BundleExtraModel(element));
    }

    @Override
    public boolean putOptionalElement(VariableElement element) {
        return mOptionalExtras.add(new BundleExtraModel(element));
    }

    @Override
    public String getPackageName() {
        return mTargetClassModel.getPackageName();
    }

    String getTargetSimpleName() {
        return mTargetClassModel.getSimpleName();
    }

    String getTargetClassName() {
        return mTargetClassModel.getQualifiedName();
    }
}
