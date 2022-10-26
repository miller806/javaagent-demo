package com.miller.agent;

import cn.hutool.core.util.StrUtil;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author miller
 * @version 1.0.0
 */
public class MyMonitorTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        try {
            String finalClassName = className.replaceAll("/", ".");
            if (!finalClassName.contains("ApiTest")) {
                return null;
            }
            System.out.println("class文件为: [" + finalClassName + "]");
            ClassPool classPool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(this.getClass());
            classPool.insertClassPath(classPath);
            CtClass ctClass = classPool.get(finalClassName);
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods) {
                enhanceMethod(method);
            }
            return ctClass.toBytecode();
        } catch (NotFoundException | IOException | CannotCompileException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void enhanceMethod(CtMethod method) {
        String methodName = method.getName();
        if (StrUtil.contains(methodName,"main")) {
            return;
        }
        final StringBuilder source = new StringBuilder();
        // 前置增强: 打入时间戳
        // 保留原有的代码处理逻辑
        source.append("{")
                .append("long start = System.nanoTime();\n") //前置增强: 打入时间戳
                .append("$_ = $proceed($$);\n")              //调用原有代码，类似于method();($$)表示所有的参数
                .append("System.out.print(\"method:[")
                .append(methodName).append("]\");").append("\n")
                .append("System.out.println(\" cost:[\" +(System.nanoTime() - start)+ \"ns]\");") // 后置增强，计算输出方法执行耗时
                .append("}");

        ExprEditor editor = new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                methodCall.replace(source.toString());
            }
        };
        try {
            method.instrument(editor);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }
}
