package com.three.recipingeventservicebe.event.dto;

import com.three.recipingeventservicebe.event.domain.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventImageUploadResponseDto {
    private ImageInfo preview;
    private ImageInfo main;
}
