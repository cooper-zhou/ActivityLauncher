package com.kyle.support.activitystarter.compiler.generator;

import com.kyle.support.activitystarter.compiler.model.BundleExtraModel;
import com.kyle.support.activitystarter.compiler.model.BundleExtraType;
import com.kyle.support.activitystarter.compiler.utils.Android;
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
public class ActivityStarterGenerator extends BaseCodeGenerator {

    private final static String STARTER_SIMPLE_NAME = "Starter";

    /**
     * a generator for inner class {Builder}.
     */
    private CodeGenerator mBuilderGenerator;

    public ActivityStarterGenerator(PackageElement packageElement, TypeElement classElement) {
        super(packageElement, classElement);
        mBuilderGenerator = new ActivityStarterBuilderGenerator(this, packageElement, classElement);
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
                .addMethod(generateStartMethod())
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
     * </code></pre>
     */
    private MethodSpec generateStartMethod() {
        return MethodSpec.methodBuilder("start")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(Android.Context.className(), "context")
                .addParameter(Android.Intent.className(), "intent")
                .addCode("context.startActivity(intent);\n")
                .build();
    }

    /**
     * Generate a method to fill the bundle data for the started activity.
     *
     * <pre><code>
     *     public static void fill(MainActivity activity, Bundle savedInstanceState) {
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
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            String valueGetMethod = bundleGetValueMethod(requiredExtra);
            fillMethod.addCode("activity.$L = ($T) savedInstanceState.$L($S);\n", requiredExtra.getName(), requiredExtra.getType(), valueGetMethod, requiredExtra.getName());
        }
        for (BundleExtraModel optionalExtra : mOptionalExtras) {
            String valueGetMethod = bundleGetValueMethod(optionalExtra);
            fillMethod.addCode("activity.$L = ($T) savedInstanceState.$L($S);\n", optionalExtra.getName(), optionalExtra.getType(), valueGetMethod, optionalExtra.getName());
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
