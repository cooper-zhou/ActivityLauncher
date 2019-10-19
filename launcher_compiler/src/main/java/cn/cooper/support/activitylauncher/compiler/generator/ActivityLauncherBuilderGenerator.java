package cn.cooper.support.activitylauncher.compiler.generator;

import cn.cooper.support.activitylauncher.annotation.Optional;
import cn.cooper.support.activitylauncher.annotation.Required;
import cn.cooper.support.activitylauncher.compiler.model.BundleExtraModel;
import cn.cooper.support.activitylauncher.compiler.utils.Android;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * {@link CodeGenerator} for ActivityStarter.Builder.
 */
public class ActivityLauncherBuilderGenerator extends BaseCodeGenerator {

    private final static String BUILDER_SIMPLE_NAME = "Builder";

    private String mOuterQualifiedName;
    private String mBuilderQualifiedName;

    ActivityLauncherBuilderGenerator(CodeGenerator outerCodeGenerator, PackageElement packageElement, TypeElement typeElement) {
        super(packageElement, typeElement);
        mOuterQualifiedName = outerCodeGenerator.getQualifiedName();
        mBuilderQualifiedName = outerCodeGenerator.getQualifiedName() + "." + BUILDER_SIMPLE_NAME;
    }

    @Override
    public String getSimpleName() {
        return BUILDER_SIMPLE_NAME;
    }

    @Override
    public String getQualifiedName() {
        return mBuilderQualifiedName;
    }

    @Override
    public TypeSpec generateCode() {
        return TypeSpec.classBuilder(BUILDER_SIMPLE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addFields(generateFields())
                .addMethod(generateConstructor())
                .addMethods(generateOptionalMethods())
                .addMethod(generateStartMethod(false))
                .addMethod(generateStartMethod(true))
                .build();
    }

    /**
     * Both required and optional arguments are Builder's fields.
     */
    private List<FieldSpec> generateFields() {
        List<FieldSpec> fieldSpecList = new ArrayList<>();
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            FieldSpec fieldSpec = FieldSpec.builder(requiredExtra.getType(), requiredExtra.getName(), Modifier.PRIVATE).build();
            fieldSpecList.add(fieldSpec);
        }
        for (BundleExtraModel optionalExtra : mOptionalExtras) {
            FieldSpec fieldSpec = FieldSpec.builder(optionalExtra.getType(), optionalExtra.getName(), Modifier.PRIVATE).build();
            fieldSpecList.add(fieldSpec);
        }
        return fieldSpecList;
    }

    /**
     * The required arguments are add into Builder's constructor's parameters.
     * {@link Required}
     *
     * <pre><code>
     *    Builder(int required1, int required2) {
     *       this.required1 = required1;
     *       this.required2 = required2;
     *    }
     * </code></pre>
     */
    private MethodSpec generateConstructor() {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            constructorBuilder.addParameter(requiredExtra.getType(), requiredExtra.getName())
                    .addCode("this.$L = $L;\n", requiredExtra.getName(), requiredExtra.getName());
        }
        return constructorBuilder.build();
    }

    /**
     * The optional arguments are initialized by Builder's setter methods
     * {@link Optional}
     *
     * <pre><code>
     *    public Builder optional1(int optional1) {
     *       this.optional1 = optional1;
     *       return this;
     *    }
     *
     *    public Builder optional2(int optional2) {
     *       this.optional2 = optional2;
     *       return this;
     *    }
     * </code></pre>
     */
    private List<MethodSpec> generateOptionalMethods() {
        List<MethodSpec> methodSpecList = new ArrayList<>();
        for (BundleExtraModel optionalExtra : mOptionalExtras) {
            MethodSpec methodSpec = MethodSpec.methodBuilder(optionalExtra.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.bestGuess(mBuilderQualifiedName))
                    .addParameter(optionalExtra.getType(), optionalExtra.getName())
                    .addCode("this.$L = $L;\n", optionalExtra.getName(), optionalExtra.getName())
                    .addCode("return this;\n")
                    .build();
            methodSpecList.add(methodSpec);
        }
        return methodSpecList;
    }

    /**
     * Generate a method to start an Activity.
     *
     * <pre><code>
     *    public void start(Context context) {
     *       Intent intent = new Intent(context, MainActivity.class);
     *       intent.putExtra("required1", required1);
     *       intent.putExtra("required2", required2);
     *       intent.putExtra("optional1", optional1);
     *       intent.putExtra("optional2", optional2);
     *       MainActivityStarter.start(context, intent);
     *    }
     *
     *    or
     *
     *    public void startForResult(Activity activity, int requestCode) {
     *       Intent intent = new Intent(context, MainActivity.class);
     *       intent.putExtra("required1", required1);
     *       intent.putExtra("required2", required2);
     *       intent.putExtra("optional1", optional1);
     *       intent.putExtra("optional2", optional2);
     *       MainActivityStarter.startForResult(activity, intent, requestCode);
     *    }
     * </code></pre>
     *
     * PS: The launch context will be limit into activity when it start with animation or start for result.
     */
    private MethodSpec generateStartMethod(boolean forResult) {
        Android launchContext = getLaunchContext(forResult);
        String contextParam = launchContext == Android.Activity ? "activity" : "context";
        MethodSpec.Builder startMethod = MethodSpec.methodBuilder(forResult ? "startForResult" : "start")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(launchContext.className(), contextParam)
                .addCode("$T intent = new $T($L, $T.class);\n", Android.Intent.className(), Android.Intent.className(), contextParam, ClassName.bestGuess(getTargetClassName()));
        for (BundleExtraModel requiredExtra : mRequiredExtras) {
            startMethod.addCode("intent.putExtra($S, $L);\n", requiredExtra.getName(), requiredExtra.getName());
        }
        for (BundleExtraModel optionalExtra : mOptionalExtras) {
            startMethod.addCode("intent.putExtra($S, $L);\n", optionalExtra.getName(), optionalExtra.getName());
        }
        if (hasLauncherFlags()) {
            // To set up the launcher mode.
            int[] launcherFlags = getLauncherFlags();
            StringBuilder allFlags = new StringBuilder();
            for (int launcherFlag : launcherFlags) {
                allFlags.append(launcherFlag).append("|");
            }
            allFlags.deleteCharAt(allFlags.length() - 1);
            startMethod.addCode("intent.addFlags($L);\n", allFlags);
        }
        if (forResult) {
            // startForResult
            startMethod.addParameter(int.class, "requestCode");
            startMethod.addCode("$T.startForResult($L, intent, requestCode);\n", ClassName.bestGuess(mOuterQualifiedName), contextParam);
        } else {
            startMethod.addCode("$T.start($L, intent);\n", ClassName.bestGuess(mOuterQualifiedName), contextParam);
        }
        return startMethod.build();
    }
}
