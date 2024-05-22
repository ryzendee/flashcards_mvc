package com.app.flashcards.integration;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ITBase {

    private static final String POSTGRES_IMAGE = "postgres:15.6";
    private static final String REDIS_IMAGE = "redis:7.2.4";
    private static final String MINIO_IMAGE = "minio/minio:RELEASE.2024-02-17T01-15-57Z.fips";
    private static final String MINIO_ENDPOINT = "minio.endpoint";
    private static final String MINIO_ACCESS = "minio.access-key";
    private static final String MINIO_SECRET = "minio.secret-key";

    @ServiceConnection
    static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE);
    @ServiceConnection
    static final RedisContainer redisContainer = new RedisContainer(REDIS_IMAGE);
    static final MinIOContainer minioContainer = new MinIOContainer(MINIO_IMAGE);


    static {
        postgreSQLContainer.start();
        redisContainer.start();
        minioContainer.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add(MINIO_ENDPOINT, minioContainer::getS3URL);
        registry.add(MINIO_ACCESS, minioContainer::getUserName);
        registry.add(MINIO_SECRET, minioContainer::getPassword);
    }

}
