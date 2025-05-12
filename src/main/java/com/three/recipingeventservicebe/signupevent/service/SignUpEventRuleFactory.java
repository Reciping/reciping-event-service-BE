package com.three.recipingeventservicebe.signupevent.service;

import org.springframework.stereotype.Component;

@Component
public class SignUpEventRuleFactory {
    public SignUpEventRule createDefaultRule() {
        return new SignUpEventRule(3, 100000, 7); // 선착순 3명, 10만 포인트, 7일 제한
    }
}
