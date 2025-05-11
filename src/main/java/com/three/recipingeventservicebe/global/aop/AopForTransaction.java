package com.three.recipingeventservicebe.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopForTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 락 해제됐는데 응보 데이터가 롤백되면 중복 참여 허용이라는 심각한 버그가 생기기 때문에 'REQUIRES_NEW'로 트랜잭션을 분리
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
/**
 * AOP 내부 메서드인 joinPoint.proceed()에는 원래의 @Transactional이 적용되지 않기 때문에,
 * 내부에서 명시적으로 트랜잭션을 감싸주기 위해 별도 컴포넌트인 AopForTransaction이 필요
 * */
