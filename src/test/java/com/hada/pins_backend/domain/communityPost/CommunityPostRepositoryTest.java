package com.hada.pins_backend.domain.communityPost;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.communityPin.CommunityPin;
import com.hada.pins_backend.domain.communityPin.CommunityPinRepository;
import com.hada.pins_backend.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    void createCommunityPin(){
        CommunityPin communityPin = CommunityPin.builder()
                .superUser(userRepository.findAll().get(0))
                .title("커뮤니티 핀 제목")
                .content("content area")
                .category("아파트")
                .setGender(Gender.Both)
                .minAge(20)
                .maxAge(40)
                .setLimit(20)
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