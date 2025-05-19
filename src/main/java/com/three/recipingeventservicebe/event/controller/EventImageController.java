package com.three.recipingeventservicebe.event.controller;

import com.three.recipingeventservicebe.common.Response;
import com.three.recipingeventservicebe.event.dto.EventImageUploadResponseDto;
import com.three.recipingeventservicebe.event.service.EventImageUploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/images")
public class EventImageController {

    private final EventImageUploadService imageUploadService;

    @PostMapping
    public Response<EventImageUploadResponseDto> uploadEventImages(
            @RequestPart("preview") MultipartFile previewImage,
            @RequestPart("main") MultipartFile mainImage
    ) throws IOException {
        EventImageUploadResponseDto response = imageUploadService.uploadImages(previewImage, mainImage);
        return Response.ok(response);
    }
}

