package com.yoake.tools;



public class R2Log {
    public interface LogDelegate {
        void e(final String tag, final String msg, final Object ... obj);
        void w(final String tag, final String msg, final Object ... obj);
        void i(final String tag, final String msg, final Object ... obj);
        void d(final String tag, final String msg, final Object ... obj);
        void printErrStackTrace(String tag, Throwable tr, final String format, final Object ... obj);
    }

    private static LogDelegate sDelegate = null;

    public static void setDelegate(LogDelegate delegate) {
        sDelegate = delegate;
    }

    public static void e(final String tag, final String msg, final Object ... obj) {
        if (sDelegate != null) {
            sDelegate.e(tag, msg, obj);
        }
    }

    public static void w(final String tag, final String msg, final Object ... obj) {
        if (sDelegate != null) {
            sDelegate.w(tag, msg, obj);
        }
    }

    public static void i(final String tag, final String msg, final Object ... obj) {
        if (sDelegate != null) {
            sDelegate.i(tag, msg, obj);
        }
    }

    public static void d(final String tag, final String msg, final Object ... obj) {
        if (sDelegate != null) {
            sDelegate.d(tag, msg, obj);
        }
    }

    public static void printErrStackTrace(String tag, Throwable tr, final String format, final Object ... obj) {
        if (sDelegate != null) {
            sDelegate.printErrStackTrace(tag, tr, format, obj);
        }
    }
}
