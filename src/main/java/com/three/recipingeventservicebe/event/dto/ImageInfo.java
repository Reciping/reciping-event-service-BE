package com.three.recipingeventservicebe.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
    private String objectName;  // ex: preview.jpg
    private String keyName;     // ex: UUID-preview.jpg
    private String filePath;    // ex: S3 또는 CloudFront URL
}
