package com.HotelBookingService.HotelBookingBackend.RoomsModule.cloud;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.pwd.accesskeyid}")
    private String accessKeyID;
    @Value("${aws.pwd.secretAccessKey}")
    private String secretAccessKey;


    private S3Client setupCreds(){
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyID,secretAccessKey);
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).build();

    }

    public String uploadFile(@NotNull MultipartFile image, String imgName) {

        try{
            S3Client s3Client = setupCreds();
            String key = "hotelManagementPhotos/"+imgName;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("vduddu")
                    .key(key)
                    .contentType(image.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            RequestBody requestBody = RequestBody.fromInputStream(image.getInputStream(),image.getSize());
            s3Client.putObject(putObjectRequest, requestBody);
            var url = s3Client.utilities().getUrl(GetUrlRequest.builder()
                            .bucket("vduddu")
                            .key(key)
                            .region(Region.US_EAST_1)
                    .build());
            return url.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
