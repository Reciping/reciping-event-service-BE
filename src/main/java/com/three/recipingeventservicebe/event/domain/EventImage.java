package com.three.recipingeventservicebe.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventImage {
    private ImageInfo preview;
    private ImageInfo main;
}
