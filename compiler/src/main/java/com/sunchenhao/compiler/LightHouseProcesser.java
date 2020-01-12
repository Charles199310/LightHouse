package com.sunchenhao.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.sunchenhao.lighthouse.LightHouse;
import com.sunchenhao.lighthouse.annotation.Module;
import com.sunchenhao.lighthouse.annotation.Router;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @description:
 * @author: 孙晨昊
 * @date: 2020/1/11
 **/
@AutoService(Processor.class)
@SupportedSourceVersion(value= SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.sunchenhao.lighthouse.annotation.Router", "com.sunchenhao.lighthouse.annotation.Module"})
public class LightHouseProcesser extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> modules = roundEnvironment.getElementsAnnotatedWith(Module.class);
        if (modules.isEmpty()) {
            return false;
        }

        if (modules.size() > 1) {
            throw new RuntimeException("每个module,至多只允许有一个@Module");
        }

        String moduleName = "";
        for (Element module : modules) {
            moduleName = module.getAnnotation(Module.class).name();
            break;
        }

        String activityClassMap = "activityClassMap";

        FieldSpec map = FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, Class.class),
                activityClassMap, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>()", HashMap.class)
                .build();

        MethodSpec method = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addCode(CodeBlock.of("$T.getInstance().addActivityClassMap($N);\n", LightHouse.class, activityClassMap))
                .build();

        Set<? extends Element> routers = roundEnvironment.getElementsAnnotatedWith(Router.class);
        CodeBlock.Builder builder = CodeBlock.builder();

        for (Element element : routers) {
            builder.add("$N.put($S, $T.class);\n", activityClassMap, element.getAnnotation(Router.class).path(), ClassName.get(element.asType()));
        }
        CodeBlock staticBlock = builder.build();
        TypeSpec router = TypeSpec.classBuilder(moduleName + "_Router")
                .addModifiers(Modifier.PUBLIC)
                .addField(map)
                .addJavadoc("管理各个模块的路由表\n")
                .addMethod(method)
                .addStaticBlock(staticBlock)
                .build();
        JavaFile javaFile = JavaFile.builder("com.sunchenhao.lighthouse", router)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return true;
    }
}
