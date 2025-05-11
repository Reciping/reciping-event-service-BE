package com.three.recipingeventservicebe.global.aop;

import com.three.recipingeventservicebe.global.exception.custom.TimeOutLockException;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Slf4j(topic = "RedisLockAspect")
@Aspect
@Component
@RequiredArgsConstructor
public class RedisLockAspect {

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(distributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod(); // 추후 어노테이션 확장이나 로그 용도로 사용할 수 있음

        // 파라미터 이름, 값 읽기
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], paramValues[i]);
        }

        String dynamicKey = parser.parseExpression(distributedLock.key()).getValue(context, String.class);
        String fullLockKey = distributedLock.prefix() + ":" + dynamicKey;

        RLock lock = distributedLock.useFair()
                ? redissonClient.getFairLock(fullLockKey)
                : redissonClient.getLock(fullLockKey);

        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );

            if (!isLocked) {
                throw new TimeOutLockException("락 획득 실패 : " + fullLockKey);
            }

            log.info("[LOCK 획득] {}", fullLockKey);
            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TimeOutLockException("락 중단 : " + fullLockKey);
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[LOCK 해제] {}", fullLockKey);
            }
        }
    }
}

