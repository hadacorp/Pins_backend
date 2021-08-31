package com.hada.pins_backend.domain.communityPin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Created by bangjinhyuk on 2021/08/22.
 */
@SpringBootTest
class CommunityPinRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityPinRepository communityPinRepository;

    @Autowired
    private MembersAndCommunityPinRepository membersAndCommunityPinRepository;

    @Autowired
    private MiddleMgmtAndCommunityPinRepository middleMgmtAndCommunityPinRepository;

    @Test
    void Test1(){
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

        MembersAndCommunityPin membersAndCommunityPin = MembersAndCommunityPin.builder()
                .communityMember(userRepository.findAll().get(1))
                .communityPin(communityPin)
                .build();

        MiddleMgmtAndCommunityPin middleMgmtAndCommunityPin = MiddleMgmtAndCommunityPin.builder()
                .middleManager(userRepository.findAll().get(2))
                .communityPin(communityPin)
                .build();

        membersAndCommunityPinRepository.save(membersAndCommunityPin);
        middleMgmtAndCommunityPinRepository.save(middleMgmtAndCommunityPin);

        System.out.println(membersAndCommunityPinRepository.findMembersAndCommunityPinsByCommunityPin_Id(communityPinRepository.findAll().get(0).getId()));
        System.out.println(middleMgmtAndCommunityPinRepository.findMiddleMgmtAndCommunityPinsByCommunityPin_Id(communityPinRepository.findAll().get(0).getId()));




    }
}