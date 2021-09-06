package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.service.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PinServiceImpl implements PinService{
    private final S3Uploader s3Uploader;
    private final CommunityPinRepository communityPinRepository;

    @Override
    public ResponseEntity<String> createCommunityPin(User user, RequestCreateCommunityPin requestCreateCommunityPin) {
        //이미지 넣어주는 부분 test:: null일때 허용해둠
        String uploadImageURL;
        try {
            if (requestCreateCommunityPin.getImage()==null) uploadImageURL= "없음";
            else uploadImageURL = s3Uploader.upload(requestCreateCommunityPin.getImage(), "images");
            log.info("requestCreateCommunityPin = {}",requestCreateCommunityPin);
            CommunityPin communityPin = requestCreateCommunityPin.toCommunityPin(user,uploadImageURL);
            communityPinRepository.save(communityPin);
            return ResponseEntity.status(HttpStatus.CREATED).body(communityPin.getId().toString());
        }catch (Exception e){
            log.info("Exception = {}",e.toString());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }

    }
}
