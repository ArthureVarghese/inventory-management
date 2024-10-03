package com.largegroup.inventory_api.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Configuration
@EnableAspectJAutoProxy
public class LoggerUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    @Around ("execution(public * com.largegroup.inventory_api.repository.*.*(..)) ||" +
            "execution(public * com.largegroup.inventory_api.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        logger.info("Starting Executing Method {} at time {}", joinPoint.getSignature(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss")));

        Object proceed = joinPoint.proceed();

        logger.info("Finished Executing Method {} at time {}", joinPoint.getSignature(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss")));

        long executionTime = System.currentTimeMillis() - start;

        logger.info("Method '{}' in class '{}' executed in {} ms", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringType().getSimpleName(), executionTime);
        return proceed;
    }
}
