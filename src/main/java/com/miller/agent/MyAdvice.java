package com.miller.agent;

import com.miller.agent.track.TrackContext;
import com.miller.agent.track.TrackManager;
import net.bytebuddy.asm.Advice;

import java.util.UUID;

/**
 * @author miller
 * @version 1.0.0
 */
public class MyAdvice {
    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName) {
        String linkId = TrackManager.getCurrentSpan();
        if (null == linkId) {
            linkId = UUID.randomUUID().toString();
            TrackContext.setLinkId(linkId);
        }
        String entrySpan = TrackManager.createEntrySpan();
        System.out.println("链路追踪：" + entrySpan + " " + className + "." + methodName);
    }

    @Advice.OnMethodExit()
    public static void exit(@Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName) {
        TrackManager.getExitSpan();
    }
}
