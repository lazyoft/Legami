package com.lazyoft.legami.logging;

public final class NullLogger implements ILogger {
    @Override
    public void debug(String message) {

    }

    @Override
    public void info(String message) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void setLogLevel(LogLevel newLevel) {

    }

    private NullLogger() {}

    public final static NullLogger instance = new NullLogger();
}