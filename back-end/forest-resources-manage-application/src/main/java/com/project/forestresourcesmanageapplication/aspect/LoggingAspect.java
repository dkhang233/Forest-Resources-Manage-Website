package com.project.forestresourcesmanageapplication.aspect;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.forestresourcesmanageapplication.models.AccessHistory;
import com.project.forestresourcesmanageapplication.repositories.AccessHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    
    private final AccessHistoryRepository accessHistoryRepository;

    @Pointcut("execution (* com.project.*.services.*.*(..))")
    public void method(){}

    @Around("method()")
    @Transactional
    public Object getMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        AccessHistory accessHistory = new AccessHistory();

        ObjectMapper mapper = new ObjectMapper();
        
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
        Date resultdate = new Date(startTime);
        Object[] array = joinPoint.getArgs();

        Object object = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        accessHistory.setArgument(mapper.writeValueAsString(array));
        accessHistory.setTime(sdf.format(resultdate));
        accessHistory.setPerformance(executionTime);
        accessHistory.setMethod(methodName);
        //username ?

        this.accessHistoryRepository.save(accessHistory);
        return object;
    }

}
