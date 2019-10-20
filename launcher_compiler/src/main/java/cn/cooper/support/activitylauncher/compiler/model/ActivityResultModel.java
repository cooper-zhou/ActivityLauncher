package cn.cooper.support.activitylauncher.compiler.model;

import javax.lang.model.element.ExecutableElement;

import cn.cooper.support.activitylauncher.annotation.OnActivityResult;

/**
 * model describes a onActivityResult method.
 */
public class ActivityResultModel {

    private int requestCode;
    private int resultCode;
    private String methodName;

    public ActivityResultModel(ExecutableElement element) {
        OnActivityResult onActivityResult = element.getAnnotation(OnActivityResult.class);
        if (onActivityResult != null) {
            requestCode = onActivityResult.requestCode();
            resultCode = onActivityResult.resultCode();
        }
        methodName = element.getSimpleName().toString();
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMethodName() {
        return methodName;
    }
}
