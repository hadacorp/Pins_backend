package com.hada.pins_backend.domain.storyPin;

import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
                .build();
        storyPinRepository.save(storyPin);

        StoryPinLike storyPinLike = StoryPinLike.builder()
                .storyPin(storyPin)
                .user(user2)
                .build();

        StoryPinComment storyPinComment = StoryPinComment.builder()
                .storyPin(storyPin)
                .writeUser(user3)
                .content("제가 봤어요")
                .build();

        storyPinLikeRepository.save(storyPinLike);
        storyPinCommentRepository.save(storyPinComment);

        entityManager.clear();
        System.out.println(storyPinRepository.findAll().get(0));
        System.out.println(storyPinLikeRepository.findAll().get(0).getStoryPin());
        System.out.println(storyPinCommentRepository.findAll().get(0).getWriteUser());



    }

}