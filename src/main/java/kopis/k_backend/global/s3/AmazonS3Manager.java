package kopis.k_backend.global.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Optional<File> convert(MultipartFile file) throws IOException { // 파일로 변환
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getOriginalFilename());
        file.transferTo(convertedFile);
        return Optional.of(convertedFile);
    }

    public String putS3(File uploadFile, String fileName) { // S3로 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public String generateFileName(MultipartFile file) { // 파일명 생성
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }

    public void delete(String filePath) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, filePath);
            log.info(String.valueOf(deleteObjectRequest));
            amazonS3.deleteObject(deleteObjectRequest);
            log.info("Deleted object from S3 with key: {}", filePath);
        } catch (SdkClientException e) {
            log.error("Error occurred while deleting object from S3", e);
            throw new RuntimeException("Failed to delete object from S3", e);
        }
    }

    public MediaType contentType(String fileName) {
        String[] arr = fileName.split("\\.");
        String type = arr[arr.length - 1];
        return switch (type) {
            case "txt" -> MediaType.TEXT_PLAIN;
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg" -> MediaType.IMAGE_JPEG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

}
