package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.service.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@Service
@RequiredArgsConstructor
public class PinServiceImpl implements PinService{
    private final S3Uploader s3Uploader;
    private final CommunityPinRepository communityPinRepository;

    @Override
    @Transactional
    public ResponseEntity<String> createCommunityPin(User user, RequestCreateCommunityPin requestCreateCommunityPin) {
        //이미지 넣어주는 부분 test:: null일때 허용해둠
        String uploadImageURL;
        try {
            if (requestCreateCommunityPin.getImage()==null) uploadImageURL= "없음";
            else uploadImageURL = s3Uploader.upload(requestCreateCommunityPin.getImage(), "images");
            CommunityPin communityPin = requestCreateCommunityPin.toCommunityPin(user,uploadImageURL);
            communityPinRepository.save(communityPin);
            return ResponseEntity.status(HttpStatus.CREATED).body(communityPin.getId().toString());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }

    }
}
