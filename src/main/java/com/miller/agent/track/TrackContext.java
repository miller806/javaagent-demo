package com.miller.agent.track;

/**
 * @author miller
 * @version 1.0.0
 */
public class TrackContext {
    private static final ThreadLocal<String> trackLocal = new ThreadLocal<>();

    public static void clear() {
        trackLocal.remove();
    }

    public static String getLinkId() {
        return trackLocal.get();
    }

    public static void setLinkId(String linkId) {
        trackLocal.set(linkId);
    }
}
