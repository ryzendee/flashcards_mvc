package com.app.flashcards.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    private final String endpoint;
    private final String bucketName;
    private final String secretKey;
    private final String accessKey;

    public MinioConfig(
            @NotBlank @Value("${minio.endpoint}") String endpoint,
            @NotBlank @Value("${minio.bucket-name}") String bucketName,
            @NotBlank @Value("${minio.secret-key}") String secretKey,
            @NotBlank @Value("${minio.access-key}") String accessKey) {
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }
    @Bean
    public MinioClient minioClient() throws Exception {
        //initialization
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        //creating buckets
        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!bucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        }

        return minioClient;
    }
}
