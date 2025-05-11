package com.three.recipingeventservicebe.event.dto;

import com.three.recipingeventservicebe.event.domain.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class EventSummaryResponseDto {
    private String id;
    private String title;
    private ImageInfo previewImage;
}
