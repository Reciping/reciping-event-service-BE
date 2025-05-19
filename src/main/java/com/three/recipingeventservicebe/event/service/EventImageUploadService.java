package com.three.recipingeventservicebe.event.service;

import com.three.recipingeventservicebe.event.domain.ImageInfo;
import com.three.recipingeventservicebe.event.dto.EventImageUploadResponseDto;
import com.three.recipingeventservicebe.global.s3.FileUploadService;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventImageUploadService {

    private final FileUploadService fileUploadService;

    public EventImageUploadResponseDto uploadImages(MultipartFile preview, MultipartFile main) throws IOException {
        if (preview.isEmpty() || main.isEmpty()) {
            throw new IllegalArgumentException("모든 이미지가 첨부되어야 합니다.");
        }

        String previewKey = generateKey(preview.getOriginalFilename());
        String mainKey = generateKey(main.getOriginalFilename());

        String previewUrl = fileUploadService.upload(preview, previewKey);
        String mainUrl = fileUploadService.upload(main, mainKey);

        ImageInfo previewInfo = new ImageInfo(preview.getOriginalFilename(), previewKey, previewUrl);
        ImageInfo mainInfo = new ImageInfo(main.getOriginalFilename(), mainKey, mainUrl);

        return new EventImageUploadResponseDto(previewInfo, mainInfo);
    }

    private String generateKey(String filename) {
        String uuid = UUID.randomUUID().toString();
        String sanitized = filename.replaceAll("\\s", "");
        return uuid + "-" + sanitized;
    }
}