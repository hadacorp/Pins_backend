package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bangjinhyuk on 2021/08/05.
 */
@SpringBootTest
class StoryPinRepositoryTest {
    @Autowired
    private StoryPinRepository storyPinRepository;

    @Autowired
    private StoryPinLikeRepository storyPinLikeRepository;

    @Autowired
    private StoryPinCommentRepository storyPinCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    public void insertPin() throws IOException {
        insertUser();
    }

    @Test
    @Transactional
    void Test1(){
        User user1 = userRepository.findAll().get(0);
        User user2 = userRepository.findAll().get(1);
        User user3 = userRepository.findAll().get(2);

        StoryPin storyPin = StoryPin.builder()
                .content("분실 내용")
                .category("분실")
                .createUser(user1)
                .latitude(12.1111)
                .longitude(12.1212)
                .title("제목")
                .image("123")
                .build();
        storyPinRepository.save(storyPin);

        StoryPinLike storyPinLike = StoryPinLike.builder()
                .storyPin(storyPin)
                .user(user2)
                .build();
        StoryPinLike storyPinLike1 = StoryPinLike.builder()
                .storyPin(storyPin)
                .user(user1)
                .build();
        StoryPinLike storyPinLike2 = StoryPinLike.builder()
                .storyPin(storyPin)
                .user(user3)
                .build();

        StoryPinComment storyPinComment = StoryPinComment.builder()
                .storyPin(storyPin)
                .writeUser(user3)
                .content("제가 봤어요")
                .build();

        storyPinLikeRepository.save(storyPinLike);
        storyPinLikeRepository.save(storyPinLike1);
        storyPinLikeRepository.save(storyPinLike2);
        storyPinCommentRepository.save(storyPinComment);

        entityManager.clear();
        System.out.println(storyPinRepository.findAll().get(0).getStoryPinLikes());
        System.out.println(storyPinRepository.findAll().get(0).getStoryPinComments());

        System.out.println(storyPinLikeRepository.findStoryPinLikesByStoryPin_Id(
                storyPinRepository.findAll().get(0).getId()).size());

        System.out.println(storyPinCommentRepository.findAll().get(0).getWriteUser());



    }

    private void insertUser() throws IOException {
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
    }

}