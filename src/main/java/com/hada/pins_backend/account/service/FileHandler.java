package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.exception.CProfileImageInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by parksuho on 2022/01/27.
 */
@Component
@Slf4j
public class FileHandler {
    public String parseFileInfo(MultipartFile multipartFile, String phoneNum) {
        String contentType = multipartFile.getContentType();
        String originalFileExtension;
        if (!StringUtils.hasText(contentType)) throw new CProfileImageInvalidException();
        if(contentType.contains("image/jpeg")){
            originalFileExtension = ".jpg";
        }
        else if(contentType.contains("image/png")){
            originalFileExtension = ".png";
        }
        else if(contentType.contains("image/gif")) {
            originalFileExtension = ".gif";
        } else throw new CProfileImageInvalidException();

        // 파일 이름
        String fileName = UUID.randomUUID() + originalFileExtension;
        // 파일 위치
        String filePath = "profileImage/" + phoneNum + "/" + fileName;
        log.info("profileImage : origName : \"{}\", fileName : \"{}\", filePath : \"{}\"",
                multipartFile.getOriginalFilename(), fileName, filePath);
        return filePath;
    }
}
