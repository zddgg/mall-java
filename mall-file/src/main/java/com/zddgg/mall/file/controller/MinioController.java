package com.zddgg.mall.file.controller;

import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.file.bean.UploadRes;
import com.zddgg.mall.file.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class MinioController {

    private final MinioService minioService;
    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public Result<UploadRes> upload(MultipartFile file) {
        UploadRes uploadRes = minioService.upload(file);
        return Result.success(uploadRes);
    }

}

