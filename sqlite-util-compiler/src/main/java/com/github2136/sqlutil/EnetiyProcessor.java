package com.github2136.sqlutil;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by yubin on 2017/8/4.
 */
@AutoService(Processor.class)
public class EnetiyProcessor extends AbstractProcessor {
    Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> classSet = new LinkedHashSet<>();
//        classSet.add(Column.class.getCanonicalName());
        classSet.add(Table.class.getCanonicalName());
//        Collections.singleton(Table.class.getCanonicalName());
        return classSet;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("---process---");
        Set<? extends Element> eleClasses = roundEnv.getElementsAnnotatedWith(Table.class);
        Set<? extends Element> eleFields = roundEnv.getElementsAnnotatedWith(Column.class);
        System.out.println("---class.size():" + eleClasses.size() + "---");
        System.out.println("---field.size():" + eleFields.size() + "---");
        TypeElement eleClass;
        VariableElement eleField;
        Map<String, List<FieldSpec>> varMap = new HashMap<>();

        System.out.println("---field get start---");
        for (Element ele : eleFields) {
            if (ele.getKind() == ElementKind.FIELD) {
                eleField = (VariableElement) ele;
                TypeElement enclosingElement = (TypeElement) eleField.getEnclosingElement();
                String className = enclosingElement.getQualifiedName().toString();
                System.out.println("---className:" + className + "---");
                String fieldName = eleField.getSimpleName().toString();
                System.out.println("---field:" + fieldName + "---");
                String type = eleField.asType().toString();
                System.out.println("---type:" + type + "---");
                List<FieldSpec> var = varMap.get(className);
                if (var == null) {
                    var = new ArrayList<>();
                    varMap.put(className, var);
                }
                FieldSpec fs = FieldSpec.
                        builder(String.class, "DATA_" + fieldName, Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).
                        initializer(CodeBlock.of("\"" + fieldName + "\""))
                        .build();
                var.add(fs);
            }
//            MethodSpec creaedMethod = MethodSpec.methodBuilder(ele.getSimpleName() +"_")
//                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                    .returns(void.class)
//                    .addParameter(String[].class, "parameters")
//                    .addStatement("System.out.println($S)", "this`s java source is created by dynamic")
//                    .build();
        }
        for (Map.Entry<String, List<FieldSpec>> entry : varMap.entrySet()) {
            StringBuffer sbName = new StringBuffer();
            for (FieldSpec fieldSpec : entry.getValue()) {
                sbName.append(fieldSpec.initializer)
                        .append(",");
            }
            if (sbName.length() > 0) {
                sbName.deleteCharAt(sbName.length() - 1);
            }
            FieldSpec fs = FieldSpec.
                    builder(String[].class, "Columns", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).
                    initializer(CodeBlock.of(" { " + sbName.toString() + " }"))
                    .build();
            entry.getValue().add(fs);
        }
        System.out.println("---field get end---");
        System.out.println("------------------------");
        System.out.println("---class get start---");
        for (Element ele : eleClasses) {
            if (ele.getKind() == ElementKind.CLASS) {
                eleClass = (TypeElement) ele;
                String packClassName = eleClass.getQualifiedName().toString();
                System.out.println("---packClassName:" + packClassName + "---");
                String className = eleClass.getSimpleName().toString();
                System.out.println("---class:" + className + "---");
                TypeSpec.Builder builder = TypeSpec.classBuilder(className + "_")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
                List<FieldSpec> var = varMap.get(packClassName);
                if (var != null) {
                    for (FieldSpec fieldSpec : var) {
                        builder.addField(fieldSpec);
                    }
                }
                TypeSpec typeSpec = builder.build();
                JavaFile javaFile = JavaFile.builder(getPackageName(eleClass), typeSpec).build();
                try {
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("---class get start---");
        System.out.println("---process end---");
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_6;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}
