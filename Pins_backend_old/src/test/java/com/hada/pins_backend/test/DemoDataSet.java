package com.hada.pins_backend.test;

import com.hada.pins_backend.domain.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.domain.storyPin.StoryPinCommentRepository;
import com.hada.pins_backend.domain.storyPin.StoryPinLikeRepository;
import com.hada.pins_backend.domain.storyPin.StoryPinRepository;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;

/**
 * Created by bangjinhyuk on 2021/12/01.
 */
@SpringBootTest
public class DemoDataSet {

    public static double latitude = 37.222958;
    public static double longitude = 126.974663;
    public static double latitude2 = 37.484085;
    public static double longitude2 = 126.782803;
    @Autowired
    private MeetingPinRepository meetingPinRepository;
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
}
