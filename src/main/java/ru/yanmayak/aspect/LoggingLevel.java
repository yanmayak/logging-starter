package ru.yanmayak.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoggingLevel {
    ALL(7),
    TRACE(6),
    DEBUG(5),
    INFO(4),
    WARN(3),
    ERROR(2),
    FATAL(1),
    OFF(0);

    private final int level;

}
