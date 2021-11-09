package com.hada.pins_backend.domain.communityPost;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserService;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/08/22.
 */
@SpringBootTest
class CommunityPostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityPinRepository communityPinRepository;

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @Autowired
    private CommunityPostCommentRepository communityPostCommentRepository;

    @Autowired
    private CommunityPostLikeRepository communityPostLikeRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void createCommunityPin() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/images/21b4b8ff-dd07-4838-a703-35f8f83378caman-technologist-light-skin-tone_1f468-1f3fb-200d-1f4bb.png").openStream());

        JoinUserRequest joinUserRequest = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                file);
        userService.insertUser(joinUserRequest);

        JoinUserRequest joinUserRequest2 = new JoinUserRequest("강선호",
                "강강강",
                "981214-1",
                "010-9176-0466",
                file);
        userService.insertUser(joinUserRequest2);

        JoinUserRequest joinUserRequest3 = new JoinUserRequest("주동석",
                "주주주",
                "980101-1",
                "010-8541-3962",
                file);
        userService.insertUser(joinUserRequest3);

        CommunityPin communityPin = CommunityPin.builder()
                .superUser(userRepository.findAll().get(0))
                .title("커뮤니티 핀 제목")
                .content("content area")
                .category("아파트")
                .setGender(Gender.Both)
                .minAge(20)
                .maxAge(40)
                .detail("detaile입니다")
                .participationType(0)
                .profileType(0)
                .latitude(12.123)
                .longitude(14.123)
                .build();
        communityPinRepository.save(communityPin);
    }
    @Test
    void Test1(){


        CommunityPost communityPost = CommunityPost.builder()
                .createUser(userRepository.findAll().get(0))
                .communityPin(communityPinRepository.findAll().get(0))
                .text("POST texxxxxxt")
                .image("image title")
                .video("video title")
                .latitude(12.123)
                .longitude(23.234).build();
        communityPostRepository.save(communityPost);

        CommunityPostLike communityPostLike = CommunityPostLike.builder()
                .user(userRepository.findAll().get(1))
                .communityPost(communityPost)
                .build();

        CommunityPostComment communityPostComment = CommunityPostComment.builder()
                .user(userRepository.findAll().get(2))
                .communityPost(communityPost)
                .content("commenttttts").build();


        communityPostLikeRepository.save(communityPostLike);
        communityPostCommentRepository.save(communityPostComment);

        System.out.println(communityPostLikeRepository.findAll().size());
        communityPostCommentRepository.findAll().forEach(System.out::println);
    }

}