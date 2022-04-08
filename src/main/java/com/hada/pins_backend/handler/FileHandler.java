package com.hada.pins_backend.handler;

import com.hada.pins_backend.account.exception.CProfileImageInvalidException;
import com.hada.pins_backend.model.FileFolderName;
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
 * Modified by parksuho on 2022/02/07.
 * Modified by parksuho on 2022/02/27.
 * Modified by parksuho on 2022/04/05.
 */
@Component
@Slf4j
public class FileHandler {
    @Value("${cloud.aws.env}")
    private String env;

    public String parseFileInfo(MultipartFile multipartFile, String key, FileFolderName name) {
        String contentType = multipartFile.getContentType();
        String originalFileExtension;
        if (!StringUtils.hasText(contentType)) {
            log.error("File type is empty");
            throw new CProfileImageInvalidException();
        }
        if(contentType.contains("image/jpeg") || contentType.contains("image/jpg")){
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
        String filePath = env + "/" + FileFolderName.folderNameToString(name) + "/" + key + "/" + fileName;
        log.info("profileImage : origName : \"{}\", fileName : \"{}\", filePath : \"{}\"",
                multipartFile.getOriginalFilename(), fileName, filePath);
        return filePath;
    }
}
