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
        //类
        Set<? extends Element> eleClasses = roundEnv.getElementsAnnotatedWith(Table.class);
        //字段
        Set<? extends Element> eleFields = roundEnv.getElementsAnnotatedWith(Column.class);
        System.out.println("---class.size():" + eleClasses.size() + "---");
        System.out.println("---field.size():" + eleFields.size() + "---");
        TypeElement eleClass = null;//类对象
        VariableElement eleField = null;//变量对象
        Map<String, List<FieldSpec>> varMap = new HashMap<>();//按包名+类名分别存储变量

        System.out.println("---field get start---");
        for (Element ele : eleFields) {
            //判断为变量类型
            if (ele.getKind() == ElementKind.FIELD) {
                //强转为变量
                eleField = (VariableElement) ele;
                TypeElement enclosingElement = (TypeElement) eleField.getEnclosingElement();
                //注解字段所在的包名+类
                String className = enclosingElement.getQualifiedName().toString();
                System.out.println("---className:" + className + "---");
                //注解字段名
                String fieldName = eleField.getSimpleName().toString();
                System.out.println("---field:" + fieldName + "---");
                //注解字段类型
                String type = eleField.asType().toString();
                System.out.println("---type:" + type + "---");
                List<FieldSpec> var = varMap.get(className);
                if (var == null) {
                    var = new ArrayList<>();
                    varMap.put(className, var);
                }
                FieldSpec fs = FieldSpec.
                        //设置数据类型
                                builder(String.class, "DATA_" + fieldName, Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).
                        //初始化值
                                initializer(CodeBlock.of("\"" + fieldName + "\""))
                        .build();
                var.add(fs);
            }
            //方法添加
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
                    //设置数据类型
                            builder(String[].class, "Columns", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC).
                    //初始化值
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
                //注解字段所在的包名+类
                String packClassName = eleClass.getQualifiedName().toString();
                System.out.println("---packClassName:" + packClassName + "---");
                //类名
                String className = eleClass.getSimpleName().toString();
                System.out.println("---class:" + className + "---");
                //创建类信息
                TypeSpec.Builder builder = TypeSpec.classBuilder(className + "_")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
                //添加变量
                List<FieldSpec> var = varMap.get(packClassName);
                if (var != null) {
                    for (FieldSpec fieldSpec : var) {
                        builder.addField(fieldSpec);
                    }
                }
                TypeSpec typeSpec = builder.build();
                //生成文件
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

    /**
     * 获取包名
     *
     * @param type
     * @return
     */
    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}
