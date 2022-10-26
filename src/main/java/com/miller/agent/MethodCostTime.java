package com.miller.agent;


import cn.hutool.core.lang.Console;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author miller
 * @version 1.0.0
 */
public class MethodCostTime {
    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            Console.log("{}方法耗时:{}ms", method.getName(), System.currentTimeMillis() - start);
        }
    }
}
