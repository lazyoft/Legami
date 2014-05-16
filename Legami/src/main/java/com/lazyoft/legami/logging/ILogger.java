package com.lazyoft.legami.logging;

public interface ILogger {
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void setLogLevel(LogLevel newLevel);
}
