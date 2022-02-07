package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.exception.CProfileImageInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by parksuho on 2022/01/27.
 * Modified by parksuho on 2022/01/31.
 */
@Component
@Slf4j
public class FileHandler {
    @Value("${cloud.aws.env}")
    private String env;

    public String parseFileInfo(MultipartFile multipartFile, String phoneNum) {
        String contentType = multipartFile.getContentType();
        String originalFileExtension;
        if (!StringUtils.hasText(contentType)) {
            log.error("file string is empty");
            throw new CProfileImageInvalidException();
        }
        if(contentType.contains("image/jpeg")){
            originalFileExtension = ".jpg";
        }
        else if(contentType.contains("image/png")){
            originalFileExtension = ".png";
        }
        else if(contentType.contains("image/gif")) {
            originalFileExtension = ".gif";
        } else  {
            log.error("File type error");
            throw new CProfileImageInvalidException();
        }

        // 파일 이름
        String fileName = UUID.randomUUID() + originalFileExtension;
        // 파일 위치
        String filePath = env + "/" + "profileImage/" + phoneNum + "/" + fileName;
        log.info("profileImage : origName : \"{}\", fileName : \"{}\", filePath : \"{}\"",
                multipartFile.getOriginalFilename(), fileName, filePath);
        return filePath;
    }
}
