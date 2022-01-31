package com.hada.pins_backend.handler;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho on 2022/01/27.
 * Modified by parksuho on 2022/01/30.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {
    @Value("${cloud.aws.cloudFront.distributionDomain}")
    public String CLOUD_FRONT_DOMAIN_NAME;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public String upload(MultipartFile multipartFile, String fileName) throws IOException {
        log.info("upload file : " + multipartFile);
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        upload(uploadFile, fileName);
        return fileName;
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String fileName) {
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("File putS3 success");
        return amazonS3Client.getUrl(bucket, fileName).toString();
        //테스트 서버용
//        return "https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/328266fd-8f20-4db7-9501-ebfbff7fa76auserimage1.png";
    }

    // get file
    private String getS3(String fileName) {
        log.info("get file : " + fileName);
        return amazonS3Client.getUrl(CLOUD_FRONT_DOMAIN_NAME, fileName).toString();
    }

    // delete file
    public void deleteFileS3(String fileName) {
        log.info("delete file : " + fileName);
        if (amazonS3Client.doesObjectExist(bucket, fileName)) amazonS3Client.deleteObject(bucket, fileName);
    }

    // delete file
    public void deleteFolderS3(String folderName) {
        log.info("delete folder : " + folderName);
//        ObjectListing objects = amazonS3Client.listObjects(bucket, folderName);
        ListObjectsRequest listObject = new ListObjectsRequest();
        listObject.setBucketName(bucket);
        listObject.setPrefix(folderName);

        ObjectListing objects;
        do {
            objects = amazonS3Client.listObjects(listObject);
            //1000개 단위로 읽음
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries())
            {
                amazonS3Client.deleteObject(bucket, objectSummary.getKey());
            }
            //objects = s3.listNextBatchOfObjects(objects); <--이녀석은 1000개 단위로만 가져옴..
            listObject.setMarker(objects.getNextMarker());
        } while (objects.isTruncated());
    }

    // update file
    public String updateS3(MultipartFile multipartFile, String oldFileName, String newFileName) throws IOException {
        deleteFileS3(oldFileName);
        return upload(multipartFile, newFileName);
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
