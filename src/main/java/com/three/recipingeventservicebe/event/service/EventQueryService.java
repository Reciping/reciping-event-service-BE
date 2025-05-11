package com.three.recipingeventservicebe.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "EventService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventQueryService {

}
