package com.github2136.sqlutil;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)
public class EnetiyProcessor extends AbstractProcessor {
    Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Column.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elememts = roundEnv.getElementsAnnotatedWith(Table.class);
        TypeElement classElement = null;
//        List<VariableElement> fields = null;

//        Map<String, List<VariableElement>> maps = new HashMap<>();

        for (Element ele : elememts) {
            classElement = (TypeElement) ele;
            TypeSpec typeSpec =
                    TypeSpec.classBuilder(  classElement.getSimpleName()+"_")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .build();

            JavaFile javaFile = JavaFile.builder(getPackageName(classElement), typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (ele.getKind() == ElementKind.CLASS) {
//                classElement = (TypeElement) ele;
//                maps.put(classElement.getQualifiedName().toString(), fields = new ArrayList<>());
//            } else if (ele.getKind() == ElementKind.FIELD) {
//
//                VariableElement varELe = (VariableElement) ele;
//
//                TypeElement enclosingElement = (TypeElement) varELe.getEnclosingElement();
//
//                String key = enclosingElement.getQualifiedName().toString();
//                fields = maps.get(key);
//                if (fields == null) {
//                    maps.put(key, fields = new ArrayList<VariableElement>());
//                }
//                fields.add(varELe);
//            }
        }

//        for (String key : maps.keySet()) {
//            if (maps.get(key).size() == 0) {
//                TypeElement typeElement = elementUtils.getTypeElement(key);
//                List<? extends Element> allMembers = elementUtils
//                        .getAllMembers(typeElement);
//                if (allMembers.size() > 0) {
//                    maps.get(key).addAll(ElementFilter.fieldsIn(allMembers));
//                }
//            }
//        }
//        TypeSpec typeSpec = TypeSpec.classBuilder("DI" + classElement.getSimpleName())
//                .superclass(TypeName.get(classElement.asType()))
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

//                .build();
//        JavaFile javaFile = JavaFile.builder(getPackageName(classElement), typeSpec)
//                .build();
//        try {
//            javaFile.writeTo(processingEnv.getFiler());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return false;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}
