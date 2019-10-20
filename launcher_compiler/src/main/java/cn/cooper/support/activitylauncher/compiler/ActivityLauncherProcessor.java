package cn.cooper.support.activitylauncher.compiler;

import com.google.auto.service.AutoService;

import cn.cooper.support.activitylauncher.annotation.Launcher;
import cn.cooper.support.activitylauncher.annotation.OnActivityResult;
import cn.cooper.support.activitylauncher.annotation.Optional;
import cn.cooper.support.activitylauncher.annotation.Required;
import cn.cooper.support.activitylauncher.compiler.generator.ActivityLauncherGenerator;
import cn.cooper.support.activitylauncher.compiler.generator.CodeGenerator;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@AutoService(Processor.class)
public class ActivityLauncherProcessor extends AbstractProcessor {

    private Map<String, CodeGenerator> mCodeGeneratorMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(Launcher.class.getCanonicalName());
        supportTypes.add(Required.class.getCanonicalName());
        supportTypes.add(Optional.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // To handle StarterBuilder annotations
        Set<? extends Element> starterBuilders = roundEnvironment.getElementsAnnotatedWith(Launcher.class);
        for (Element element : starterBuilders) {
            TypeElement classElement = (TypeElement) element;
            String fullClassName = classElement.getQualifiedName().toString();
            CodeGenerator generator = mCodeGeneratorMap.get(fullClassName);
            if (generator == null) {
                generator = new ActivityLauncherGenerator(processingEnv.getElementUtils().getPackageOf(classElement), classElement);
                mCodeGeneratorMap.put(fullClassName, generator);
            }
        }
        // To handle Required annotations
        Set<? extends Element> requiredElements = roundEnvironment.getElementsAnnotatedWith(Required.class);
        for (Element element : requiredElements) {
            VariableElement variableElement = (VariableElement) element;
            String fullClassName = ((TypeElement) variableElement.getEnclosingElement()).getQualifiedName().toString();
            CodeGenerator generator = mCodeGeneratorMap.get(fullClassName);
            if (generator != null) {
                generator.putRequiredElement(variableElement);
            }
        }
        // To handle Optional annotations
        Set<? extends Element> optionalElements = roundEnvironment.getElementsAnnotatedWith(Optional.class);
        for (Element element : optionalElements) {
            VariableElement variableElement = (VariableElement) element;
            String fullClassName = ((TypeElement) variableElement.getEnclosingElement()).getQualifiedName().toString();
            CodeGenerator generator = mCodeGeneratorMap.get(fullClassName);
            if (generator != null) {
                generator.putOptionalElement(variableElement);
            }
        }
        // To handle OnActivityResult annotations
        Set<? extends Element> resultElements = roundEnvironment.getElementsAnnotatedWith(OnActivityResult.class);
        for (Element element : resultElements) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String fullClassName = ((TypeElement) executableElement.getEnclosingElement()).getQualifiedName().toString();
            CodeGenerator generator = mCodeGeneratorMap.get(fullClassName);
            if (generator != null) {
                generator.putActivityResultElement(executableElement);
            }
        }
        for (String key : mCodeGeneratorMap.keySet()) {
            CodeGenerator generator = mCodeGeneratorMap.get(key);
            JavaFile javaFile = JavaFile.builder(generator.getPackageName(), generator.generateCode()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
                System.out.println("javaFile >>>>> " + javaFile.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mCodeGeneratorMap.clear();
        return true;
    }
}
