package com.three.recipingeventservicebe.event.domain;

public enum TriggerType {
    TIME_BASED, // 시간 기반 자동 발동 (예: 특정 일시부터 응모 가능)
    ACTION_BASED // 사용자 액션 기반 발동 (예: 회원가입, 구매 등)
}
