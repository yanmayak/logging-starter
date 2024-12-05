package ru.yanmayak.aspect;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoggingConfig {
    private boolean enabled;
    private LoggingLevel level;
}
