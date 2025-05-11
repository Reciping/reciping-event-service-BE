package com.three.recipingeventservicebe.global.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String prefix();
    String key();

    long waitTime() default 5000L; // 5초
    long leaseTime() default 3000L; // 3초
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    boolean useFair() default false; // 공정 락 여부
}

/**
 Redisson 기반 분산 락 어노테이션
 * - prefix: 락 키 접두어 (도메인 구분용, ex: "event:fc")
 * - key: SpEL 표현식으로 실제 대상 식별자 지정 (ex: "#eventId") -> 프리픽스와 키는 같은 성격의 다른 이벤트할 경우를 대비해서 필요합니다!
 * - waitTime: 락 대기 시간 (밀리초 기준, 락을 얻기 위해 대기할 최대 시간) -> 클라이언트가 최대 몇 초까지 응답을 기다릴 의사 정도의 시간
 * - leaseTime: 락 점유 유지 시간 (밀리초 기준, 자동 해제 시간) -> 락을 잡은 쪽이 작업을 몇 초 안에 끝날 것으로 예상되는 정도의 시간
 * - timeUnit: 시간 단위
 * - useFair: 공정 락 여부 (성능이 느리므로 기본은 false)
 */
