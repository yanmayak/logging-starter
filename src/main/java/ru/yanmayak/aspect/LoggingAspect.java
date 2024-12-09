package ru.yanmayak.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import ru.yanmayak.config.LoggerProperties;

@Aspect
@Slf4j
public class LoggingAspect {
    private final LoggerProperties loggerProperties;

    public LoggingAspect(LoggerProperties loggerProperties) {
        this.loggerProperties = loggerProperties;
    }

    private boolean shouldLog(LoggingLevel log) {
        return (log.ordinal() >= this.loggerProperties.getLevel().ordinal());
    }

    @Before("@annotation(LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        if(shouldLog(LoggingLevel.INFO)) {
            log.info("Вызван метод {}", joinPoint.getSignature().getName());
        }
    }

    @After("@annotation(LogExecution)")
    public void logAfter(JoinPoint joinPoint) {
        if(shouldLog(LoggingLevel.INFO)) {
            log.info("Метод {} завершен", joinPoint.getSignature().getName());
        }
    }

    @AfterThrowing(pointcut = "@annotation(LogException)", throwing = "throwable")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        if(shouldLog(LoggingLevel.WARN)) {
            log.warn("Возникло исключение {} : {} в методе: {}",
                    throwable.getClass().getName(),
                    throwable.getMessage(),
                    joinPoint.getSignature().getName()
            );
        }
    }

    @AfterReturning(value = "@annotation(LogExecution)", returning = "returnValue")
    public void logAfterReturning(JoinPoint joinPoint, Object returnValue) {
        if(shouldLog(LoggingLevel.INFO)) {
            log.info("Метод {} вернул {}", joinPoint.getSignature().getName(), returnValue);
        }
    }

    @Around("@annotation(LogAroundVoid)")
    public void logAroundVoidMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        if (shouldLog(LoggingLevel.INFO)) {
            log.info("Метод {} вызван", joinPoint.getSignature().getName());
        }
        try {
            joinPoint.proceed();
            if (shouldLog(LoggingLevel.INFO)) {
                log.info("Метод {} выполнен успешно", joinPoint.getSignature().getName());
            }
        }
        catch (Throwable throwable) {
            if (shouldLog(LoggingLevel.ERROR)) {
                log.error("Метод {} завершен с ошибкой: {} ",
                        joinPoint.getSignature().getName(),
                        throwable.getMessage()
                );
            }
            throw throwable; //throwable выбран в связи с наличием ControllerAdvice,
            // который обработает строго определенное исключение6 прописанное в сервисе
        }
    }
}
