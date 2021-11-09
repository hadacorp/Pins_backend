package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.domain.user.UserRepository;
import com.hada.pins_backend.dto.user.request.JoinUserRequest;
import com.hada.pins_backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collections;


/**
 * Created by bangjinhyuk on 2021/08/22.
 */
@SpringBootTest
class CommunityPinRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CommunityPinRepository communityPinRepository;

    @Autowired
    private MembersAndCommunityPinRepository membersAndCommunityPinRepository;

    @Autowired
    private MiddleMgmtAndCommunityPinRepository middleMgmtAndCommunityPinRepository;

    @Test
    void Test1() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file","userimage1.png" , "image/png" ,new URL("https://pinsuserimagebucket.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2021-09-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+5.22.56.png").openStream());

        JoinUserRequest joinUserRequest1 = new JoinUserRequest("방진혁",
                "뱅뱅뱅",
                "980103-1",
                "010-7760-6393",
                file);
        userService.insertUser(joinUserRequest1);

        CommunityPin communityPin = CommunityPin.builder()
                .superUser(userRepository.findAll().get(0))
                .createUser(userRepository.findAll().get(0))
                .title("커뮤니티 핀 제목")
                .content("content area")
                .category("아파트")
                .setGender(Gender.Both)
                .participationType(1)
                .profileType(1)
                .minAge(20)
                .maxAge(40)
                .detail("detaile입니다")
                .participationType(0)
                .profileType(0)
                .latitude(12.123)
                .longitude(14.123)
                .image("123")
                .detail("detail")
                .build();
        communityPinRepository.save(communityPin);

        MembersAndCommunityPin membersAndCommunityPin = MembersAndCommunityPin.builder()
                .communityMember(userRepository.findAll().get(0))
                .communityPin(communityPin)
                .build();

        MiddleMgmtAndCommunityPin middleMgmtAndCommunityPin = MiddleMgmtAndCommunityPin.builder()
                .middleManager(userRepository.findAll().get(0))
                .communityPin(communityPin)
                .build();

        membersAndCommunityPinRepository.save(membersAndCommunityPin);
        middleMgmtAndCommunityPinRepository.save(middleMgmtAndCommunityPin);

        System.out.println(membersAndCommunityPinRepository.findMembersAndCommunityPinsByCommunityPin_Id(communityPinRepository.findAll().get(0).getId()));
        System.out.println(middleMgmtAndCommunityPinRepository.findMiddleMgmtAndCommunityPinsByCommunityPin_Id(communityPinRepository.findAll().get(0).getId()));
        System.out.println(communityPinRepository.findAll().get(0));




    }
}