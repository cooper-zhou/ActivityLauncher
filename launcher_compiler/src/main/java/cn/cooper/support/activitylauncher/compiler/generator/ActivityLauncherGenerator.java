package cn.cooper.support.activitylauncher.compiler.generator;

import cn.cooper.support.activitylauncher.compiler.model.BundleExtraModel;
import cn.cooper.support.activitylauncher.compiler.model.BundleExtraType;
import cn.cooper.support.activitylauncher.compiler.utils.Android;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * {@link CodeGenerator} for ActivityStarter.
 */
public class ActivityLauncherGenerator extends BaseCodeGenerator {

    private final static String STARTER_SIMPLE_NAME = "Launcher";

    /**
     * a generator for inner class {Builder}.
     */
    private CodeGenerator mBuilderGenerator;

    public ActivityLauncherGenerator(PackageElement packageElement, TypeElement classElement) {
        super(packageElement, classElement);
        mBuilderGenerator = new ActivityLauncherBuilderGenerator(this, packageElement, classElement);
    }

    @Override
    public String getSimpleName() {
        return getTargetSimpleName() + STARTER_SIMPLE_NAME;
    }

    @Override
    public String getQualifiedName() {
        return getPackageName() + "." + getSimpleName();
    }

    @Override
    public TypeSpec generateCode() {
        String starterSimpleName = getTargetSimpleName() + STARTER_SIMPLE_NAME;
        return TypeSpec.classBuilder(starterSimpleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addType(mBuilderGenerator.generateCode())
                .addMethod(generateBuilderMethod())
                .addMethod(generateStartMethod(false))
                .addMethod(generateStartMethod(true))
                .addMethod(generateFillMethod())
                .addMethod(generateSaveMethod())
                .build();
    }

    @Override
    public boolean putRequiredElement(VariableElement element) {
        mBuilderGenerator.putRequiredElement(element);
        return super.putRequiredElement(element);
    }

    @Override
    public boolean putOptionalElement(VariableElement element) {
        mBuilderGenerator.putOptionalElement(element);
        return super.putOptionalElement(element);
    }

    /**
     * Generate a static method to new a Builder Object.
     *
     * <pre><code>
     *   public static Builder builder(int required1, int required2) {
     *       return new Builder(required1, required2);
     *   }
     * </code></pre>
     */
    private MethodSpec generateBuilderMethod() {
        ClassName builderClassName = ClassName.bestGuess(mBuilderGenerator.getQualifiedName());
        MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClassName);
        StringBuilder methodParams = new StringBuilder();
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            builderMethod.addParameter(requiredExtra.getType(), requiredExtra.getName());
            methodParams.append(requiredExtra.getName()).append(",");
        }
        // to delete the last ','
        methodParams.deleteCharAt(methodParams.lastIndexOf(","));
        builderMethod.addCode("return new $T($L);\n", builderClassName, methodParams.toString());
        return builderMethod.build();
    }

    /**
     * Generate a method to start an Activity. The method is private and invoked by Builder.start()
     *
     * <pre><code>
     *   public static void start(Context context, Intent intent) {
     *       context.startActivity(intent);
     *   }
     *
     *   or
     *
     *   public static void startForResult(Activity activity, Intent intent, int requestCode) {
     *       activity.startActivityForResult(intent, requestCode);
     *   }
     * </code></pre>
     */
    private MethodSpec generateStartMethod(boolean forResult) {
        Android launchContext = getLaunchContext(forResult);
        String contextParam = launchContext == Android.Activity ? "activity" : "context";
        MethodSpec.Builder builder = MethodSpec.methodBuilder(forResult ? "startForResult" : "start")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(launchContext.className(), contextParam)
                .addParameter(Android.Intent.className(), "intent");
        if (forResult) {
            builder.addParameter(int.class, "requestCode");
            builder.addCode("$L.startActivityForResult(intent, requestCode);\n", contextParam);
        } else {
            builder.addCode("$L.startActivity(intent);\n", contextParam);
        }
        if (hasLauncherPendingTransition() && hasLauncherPendingTransitionOnFinish()) {
            builder.addCode("$L.overridePendingTransition($L, $L);\n", contextParam, getLauncherPendingTransition(), getLauncherPendingTransitionOnFinish());
        }
        return builder.build();
    }

    /**
     * Generate a method to fill the bundle data for the started activity.
     *
     * <pre><code>
     *     public static void fill(MainActivity activity, Bundle savedInstanceState) {
     *         Intent intent = activity.getIntent();
     *         Bundle data = new Bundle();
     *         if (intent != null && intent.getExtras() != null) {
     *           data.putAll(intent.getExtras());
     *         } else {
     *           data.putAll(savedInstanceState);
     *         }
     *         activity.required1 = savedInstanceState.getInt("required1");
     *         activity.required2 = savedInstanceState.getInt("required2");
     *     }
     * </code></pre>
     */
    private MethodSpec generateFillMethod() {
        MethodSpec.Builder fillMethod = MethodSpec.methodBuilder("fill")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ClassName.bestGuess(getTargetClassName()), "activity")
                .addParameter(Android.Bundle.className(), "savedInstanceState");
        fillMethod.addCode("$T intent = activity.getIntent();\n", Android.Intent.className());
        fillMethod.addCode("$T data = new $T();\n", Android.Bundle.className(), Android.Bundle.className());
        fillMethod.addCode("if (intent != null && intent.getExtras() != null) {\n");
        fillMethod.addCode("  data.putAll(intent.getExtras());\n");
        fillMethod.addCode("} else {\n");
        fillMethod.addCode("  data.putAll(savedInstanceState);\n");
        fillMethod.addCode("}\n");
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            String valueGetMethod = bundleGetValueMethod(requiredExtra);
            fillMethod.addCode("activity.$L = ($T) data.$L($S);\n", requiredExtra.getName(), requiredExtra.getType(), valueGetMethod, requiredExtra.getName());
        }
        for (BundleExtraModel optionalExtra : mOptionalExtras) {
            String valueGetMethod = bundleGetValueMethod(optionalExtra);
            fillMethod.addCode("activity.$L = ($T) data.$L($S);\n", optionalExtra.getName(), optionalExtra.getType(), valueGetMethod, optionalExtra.getName());
        }
        return fillMethod.build();
    }

    /**
     * Return the method that uses to get value from bundle object.
     */
    private String bundleGetValueMethod(BundleExtraModel bundleExtraModel) {
        BundleExtraType bundleExtraType = bundleExtraModel.getExtraType();
        if (bundleExtraType == BundleExtraType.ParcelableArrayList) {
            String rawType = bundleExtraModel.getRawType().toString();
            String simpleRawType = rawType.substring(rawType.lastIndexOf(".") + 1, rawType.length());
            return  String.format("<%s>get%s", simpleRawType, bundleExtraType.name());
        }
        return bundleExtraType == BundleExtraType.Unknown ? "get" : "get" + bundleExtraType.name();
    }

    /**
     * Generate a method to save the data when onSaveInstanceState() is called.
     *
     * <pre><code>
     *     public static void save(MainActivity activity, Bundle outState) {
     *         outState.putInt("required1", activity.required1);
     *         outState.putInt("required2", activity.required2);
     *     }
     * </code></pre>
     */
    private MethodSpec generateSaveMethod() {
        MethodSpec.Builder saveMethod = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ClassName.bestGuess(getTargetClassName()), "activity")
                .addParameter(Android.Bundle.className(), "outState");
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            String valueGetMethod = bundlePutValueMethod(requiredExtra);
            saveMethod.addCode("outState.$L($S, activity.$L);\n", valueGetMethod, requiredExtra.getName(), requiredExtra.getName());
        }
        for (BundleExtraModel optionalExtra : mOptionalExtras) {
            String valueGetMethod = bundlePutValueMethod(optionalExtra);
            saveMethod.addCode("outState.$L($S, activity.$L);\n", valueGetMethod, optionalExtra.getName(), optionalExtra.getName());
        }
        return saveMethod.build();
    }

    /**
     * Return the method that uses to out value into bundle object.
     */
    private String bundlePutValueMethod(BundleExtraModel bundleExtraModel) {
        BundleExtraType bundleExtraType = bundleExtraModel.getExtraType();
        return bundleExtraType == BundleExtraType.Unknown ? "put" : "put" + bundleExtraType.name();
    }
}
