package com.app.flashcards.utils.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioUtilsImpl implements MinioUtils {

    private final MinioClient minioClient;

    @Override
    public void uploadFile(String bucketName, String path, MultipartFile file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        //Checking is bucket available and creating new bucket if unavailable
        boolean foundBucket = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());

        if (!foundBucket) {
            log.info("Bucket not found, trying to create: {}", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
            log.info("Bucket was created: {}", bucketName);
        }

        //Putting object
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(path)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        log.info("File was saved: {}", path);
    }
    @Override
    public String getUrlToFile(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(path)
                        .build());
    }

    @Override
    public void deleteFile(String bucketName, String path) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path)
                        .build()
        );
    }
}
