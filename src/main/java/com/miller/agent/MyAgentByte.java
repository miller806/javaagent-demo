package com.miller.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author miller
 * @version 1.0.0
 */
public class MyAgentByte {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("嗨！JavaAgent " + agentArgs);
        MyMonitorTransformer monitor = new MyMonitorTransformer();
        inst.addTransformer(monitor);
    }

    public static void premain(String agentArgs) {

    }
}
