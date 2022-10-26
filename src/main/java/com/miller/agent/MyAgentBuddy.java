package com.miller.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author miller
 * @version 1.0.0
 */
public class MyAgentBuddy {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("嗨！JavaAgent " + agentArgs);

        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule, protectionDomain) -> {
            return builder
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(MethodCostTime.class));
        };

        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {

            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {

            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };
        new AgentBuilder
                .Default()
                .type(ElementMatchers.nameStartsWith("com.miller"))
                .transform(transformer)
                .with(listener)
                .installOn(inst);


        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            JvmStack.printMemoryInfo();
            JvmStack.printGCInfo();
            System.out.println("======================");
        }, 0, 5000, TimeUnit.MILLISECONDS);

    }

    public static void premain(String agentArgs) {

    }
}
