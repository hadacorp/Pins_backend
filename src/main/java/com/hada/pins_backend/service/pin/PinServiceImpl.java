package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPin;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.request.RequestCreateCommunityPin;
import com.hada.pins_backend.dto.pin.request.RequestMeetingPin;
import com.hada.pins_backend.dto.pin.request.RequestStoryPin;
import com.hada.pins_backend.dto.pin.response.MeetingPinResponse;
import com.hada.pins_backend.dto.pin.response.StoryPinResponse;
import com.hada.pins_backend.exception.pin.NotExistException;
import com.hada.pins_backend.service.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by bangjinhyuk on 2021/09/06.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PinServiceImpl implements PinService{
    private final S3Uploader s3Uploader;
    private final CommunityPinRepository communityPinRepository;
    private final MeetingPinRepository meetingPinRepository;
    private final StoryPinRepository storyPinRepository;

    /**
     * 커뮤니티핀 저장
     */
    @Override
    @Transactional
    public ResponseEntity<String> createCommunityPin(User user, RequestCreateCommunityPin requestCreateCommunityPin) {
        try {
            //이미지 넣어주는 부분 test:: null일때 허용해둠
            String uploadImageURL;
            if (requestCreateCommunityPin.getImage()==null) uploadImageURL= "없음";
            else uploadImageURL = s3Uploader.upload(requestCreateCommunityPin.getImage(), "images", requestCreateCommunityPin.getTitle());
            log.info("requestCreateCommunityPin = {}",requestCreateCommunityPin);
            CommunityPin communityPin = requestCreateCommunityPin.toCommunityPin(user,uploadImageURL);
            communityPinRepository.save(communityPin);
            return ResponseEntity.status(HttpStatus.CREATED).body(communityPin.getId().toString());
        }catch (Exception e){
            log.info("Exception = {}",e.toString());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("저장 실패");
        }

    }

    /**
     * 만남핀 저장
     */
    @Override
    @Transactional
    public ResponseEntity<String> createMeetingPin(User user, RequestMeetingPin requestMeetingPin) {
        try{
            LocalDateTime renameDate = LocalDateTime.now().plusDays(requestMeetingPin.getDate());
            renameDate = LocalDateTime.of(renameDate.getYear(),renameDate.getMonth(), renameDate.getDayOfMonth(), requestMeetingPin.getHour() , requestMeetingPin.getMinute());
            MeetingPin meetingPin = requestMeetingPin.toMeetingPin(user,renameDate);
            meetingPinRepository.save(meetingPin);
            return ResponseEntity.status(HttpStatus.CREATED).body(meetingPin.getId().toString());
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("저장 실패");
        }

    }

    /**
     * 이야기핀 저장
     */
    @Override
    public ResponseEntity<String> createStoryPin(User user, RequestStoryPin requestStoryPin) {
        log.info("{}",        requestStoryPin.toString());
        try{
            //이미지 넣어주는 부분
            String uploadImageURL;
            if (requestStoryPin.getImage()==null) uploadImageURL= "none";
            else uploadImageURL = s3Uploader.upload(requestStoryPin.getImage(), "images", requestStoryPin.getTitle());
            StoryPin storyPin = requestStoryPin.toStoryPin(user,uploadImageURL);
            storyPinRepository.save(storyPin);
            return ResponseEntity.status(HttpStatus.CREATED).body(storyPin.getId().toString());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("저장 실패");

        }
    }
    /**
     * 만남핀 가져오기
     */
    @Override
    public ResponseEntity<MeetingPinResponse> getMeetingPin(Long id) {
        if(meetingPinRepository.findById(id).isPresent()) {
            MeetingPin meetingPin = meetingPinRepository.findById(id).get();
            return ResponseEntity.ok(new MeetingPinResponse().meetingPintoResponse(meetingPin));
        }else throw new NotExistException();
    }
    /**
     * 이야기핀 가져오기
     */
    @Override
    public ResponseEntity<StoryPinResponse> getStoryPin(Long id) {
        if(storyPinRepository.findById(id).isPresent()) {
            StoryPin storyPin = storyPinRepository.findById(id).get();
            return ResponseEntity.ok(new StoryPinResponse().storyPintoResponse(storyPin));
        }else throw new NotExistException();
    }
    /**
     * 커뮤니티핀 삭제
     */
    @Override
    public ResponseEntity<String> deleteCommunityPin(Long id) {
        if(communityPinRepository.findById(id).isPresent()){
            communityPinRepository.deleteById(id);
            return ResponseEntity.ok("delete");
        }else return ResponseEntity.ok("Not exist");
    }
    /**
     * 만남핀 삭제
     */
    @Override
    public ResponseEntity<String> deleteMeetingPin(Long id) {
        if(meetingPinRepository.findById(id).isPresent()){
            meetingPinRepository.deleteById(id);
            return ResponseEntity.ok("delete");
        }else return ResponseEntity.ok("Not exist");
    }
    /**
     * 이야기핀 삭제
     */
    @Override
    public ResponseEntity<String> deleteStoryPin(Long id) {
        if(storyPinRepository.findById(id).isPresent()){
            storyPinRepository.deleteById(id);
            return ResponseEntity.ok("delete");
        }else return ResponseEntity.ok("Not exist");
    }
}
