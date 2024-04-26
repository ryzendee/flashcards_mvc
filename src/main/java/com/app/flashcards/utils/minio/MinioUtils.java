package com.app.flashcards.utils.minio;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinioUtils {
    void uploadFile(String bucketName, String path, MultipartFile file) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException;
    void deleteFile(String bucketName, String path) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException;
    String getUrlToFile(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
