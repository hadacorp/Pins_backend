package com.hada.pins_backend.test;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.MeetingPin;
import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.*;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Created by bangjinhyuk on 2021/09/11.
 */
@SpringBootTest
public class InsertTestDataSet {
    public static double latitude = 37.222958;
    public static double longitude = 126.974663;
    @Autowired
    private MeetingPinRepository meetingPinRepository;
    @Autowired
    private StoryPinRepository storyPinRepository;
    @Autowired
    private StoryPinLikeRepository storyPinLikeRepository;
    @Autowired
    private StoryPinCommentRepository storyPinCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void insertUser() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.56.png").openStream());

        JoinUserRequest joinUserRequest1 = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                file);
        userService.insertUser(joinUserRequest1);
        JoinUserRequest joinUserRequest2 = new JoinUserRequest("강선호",
                "강강강",
                "981214-1",
                "010-9176-0466",
                file);
        userService.insertUser(joinUserRequest2);
        JoinUserRequest joinUserRequest3 = new JoinUserRequest("주동석",
                "주주주",
                "981111-1",
                "010-8541-3962",
                file);
        userService.insertUser(joinUserRequest3);
        JoinUserRequest joinUserRequest4 = new JoinUserRequest("이승현",
                "이승이",
                "980610-1",
                "010-9266-9474",
                file);
        userService.insertUser(joinUserRequest4);
        JoinUserRequest joinUserRequest5 = new JoinUserRequest("이범수",
                "이범이",
                "980323-1",
                "010-4043-3055",
                file);
        userService.insertUser(joinUserRequest5);

    }

    @Test
    public void insertMeetingPin(){
        String [] meetingPinCategory = new String[11];
        String [] storyPinCategory = new String[9];
        String [] storypinImage = new String[8];
        String allMeetingPinCategory = "대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-거래/나눔-기타";
        String allStoryPinCategory = "#궁금해요-#장소리뷰-#동네꿀팁-#반려동물-#취미생횔-#도와줘요-#사건사고-#분실/실종-#잡담";
        StringTokenizer stringTokenizer = new StringTokenizer(allMeetingPinCategory,"-");
        StringTokenizer stringTokenizer2 = new StringTokenizer(allStoryPinCategory,"-");
        for (int i=0;i<11;i++){
            meetingPinCategory[i] = stringTokenizer.nextToken();
        }
        for (int i=0;i<9;i++){
            storyPinCategory[i] = stringTokenizer2.nextToken();
        }
        for(int i=0;i<700;i++){
            int userNum = (int)((Math.random()*10000)%5);
            int categoryNum = (int)((Math.random()*10000)%11);
            int dayNum = (int)((Math.random()*10000)%8);
            double latitudeValue = Math.random()/10;
            double longitudeValue = Math.random()/10;
            User user = userRepository.findAll().get(userNum);

            meetingPinRepository.save(MeetingPin.builder()
                    .createUser(user)
                    .title(meetingPinCategory[categoryNum]+"<= 카테고리 :만남핀 TEST: 생성 유저=>"+user.getName())
                    .content(user.getNickName()+" === "+user.getPhoneNum())
                    .minAge(userNum+categoryNum+dayNum+10)
                    .maxAge(userNum+categoryNum+dayNum+20)
                    .setLimit(categoryNum+1)
                    .setGender(Gender.Both)
                    .category(meetingPinCategory[categoryNum])
                    .latitude(latitude + latitudeValue)
                    .longitude(longitude + longitudeValue)
                    .date(LocalDateTime.now().plusDays(dayNum))
                    .build());

        }
        storypinImage[0]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.04.25.png";
        storypinImage[1]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.04.45.png";
        storypinImage[2]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.05.43.png";
        storypinImage[3]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.05.50.png";
        storypinImage[4]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.06.00.png";
        storypinImage[5]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.06.08.png";
        storypinImage[6]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.06.17.png";
        storypinImage[7]="https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+6.06.21.png";
        for(int i=0;i<300;i++){
            int userNum = (int)((Math.random()*10000)%5);
            int categoryNum = (int)((Math.random()*10000)%9);
            int imageNum = (int)((Math.random()*10000)%8);
            double latitudeValue = Math.random()/10;
            double longitudeValue = Math.random()/10;
            User user = userRepository.findAll().get(userNum);

            storyPinRepository.save(StoryPin.builder()
                    .createUser(user)
                    .title(storyPinCategory[categoryNum]+"<= 카테고리 :이야기핀 TEST: 생성 유저=>"+user.getName())
                    .content(user.getNickName()+" === "+user.getPhoneNum())
                    .category(storyPinCategory[categoryNum])
                    .latitude(latitudeValue+latitude)
                    .longitude(longitudeValue+longitude)
                    .image(storypinImage[imageNum])
                    .build());

        }
        for(int i=0;i<400;i++){
            int userNum1 = (int)((Math.random()*10000)%5);
            int userNum2 = (int)((Math.random()*10000)%5);
            int storypinNum1 = (int)((Math.random()*10000)%200);
            int storypinNum2 = (int)((Math.random()*10000)%200);

            User user1 = userRepository.findAll().get(userNum1);
            User user2 = userRepository.findAll().get(userNum2);

            StoryPinLike storyPinLike1 = StoryPinLike.builder().storyPin(storyPinRepository.getById(storypinNum1+1L)).user(user1).build();
            storyPinLikeRepository.save(storyPinLike1);

            StoryPinComment storyPinComment1 = StoryPinComment.builder().storyPin(storyPinRepository.getById(storypinNum2+1L)).content("댓댓댓글그륵ㄹ").writeUser(user2).build();
            storyPinCommentRepository.save(storyPinComment1);

        }
    }
}
