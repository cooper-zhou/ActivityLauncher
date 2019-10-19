package cn.cooper.support.activitylauncher.compiler.generator;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import cn.cooper.support.activitylauncher.compiler.model.BundleExtraModel;
import cn.cooper.support.activitylauncher.compiler.model.LauncherModel;
import cn.cooper.support.activitylauncher.compiler.utils.Android;

public abstract class BaseCodeGenerator implements CodeGenerator {

    private LauncherModel mTargetLauncherModel;

    List<BundleExtraModel> mRequiredExtras = new ArrayList<>();
    List<BundleExtraModel> mOptionalExtras = new ArrayList<>();

    BaseCodeGenerator(PackageElement packageElement, TypeElement typeElement) {
        mTargetLauncherModel = new LauncherModel(packageElement, typeElement);
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
        return mTargetLauncherModel.getPackageName();
    }

    String getTargetSimpleName() {
        return mTargetLauncherModel.getSimpleName();
    }

    String getTargetClassName() {
        return mTargetLauncherModel.getQualifiedName();
    }

    boolean hasLauncherFlags() {
        return getLauncherFlags().length > 0;
    }

    int[] getLauncherFlags() {
        return mTargetLauncherModel.getFlags();
    }

    boolean hasLauncherPendingTransition() {
        return getLauncherPendingTransition() != 0;
    }

    int getLauncherPendingTransition() {
        return mTargetLauncherModel.getPendingTransition();
    }

    boolean hasLauncherPendingTransitionOnFinish() {
        return getLauncherPendingTransitionOnFinish() != 0;
    }

    int getLauncherPendingTransitionOnFinish() {
        return mTargetLauncherModel.getPendingTransitionOnFinish();
    }

    Android getLaunchContext(boolean forResult) {
        return forResult ? Android.Activity : mTargetLauncherModel.getLaunchContext();
    }
}
