package com.example.dropthefishbackendrdb.fish.controller;

import com.example.dropthefishbackendrdb.fish.service.FishImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fish")
public class FishImageController {
    private final FishImageService fishImageService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return fishImageService.uploadImage(file);
    }

    @GetMapping("/image")
    public String loadImage() {
        return "";
    }

    @PostMapping("/analyze")
    @ResponseStatus(value = HttpStatus.OK)
    public String analyzeImage(@RequestParam("file") MultipartFile fishImage) {
        String encodedImage = fishImageService.encodeImage(fishImage);
        return fishImageService.analyzeImage(encodedImage);
    }

    @PostMapping("/analyze/sushi")
    @ResponseStatus(value = HttpStatus.OK)
    public String analyzeSushiImage(@RequestParam("file") MultipartFile fishImage) {
        String encodedImage = fishImageService.encodeImage(fishImage);
        return fishImageService.anlayzeSushiImage(encodedImage);
    }
}
