package com.hada.pins_backend.pin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hada.pins_backend.ControllerTest;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.entity.dto.LoginUserRequest;
import com.hada.pins_backend.account.model.entity.dto.TokenDto;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinParticipants;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinRequest;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinParticipants;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinRequest;
import com.hada.pins_backend.pin.model.enumable.Position;
import com.hada.pins_backend.pin.model.enumable.State;
import com.hada.pins_backend.pin.model.request.CreateCommunityPinRequest;
import com.hada.pins_backend.pin.model.request.CreateMeetingPinRequest;
import com.hada.pins_backend.pin.model.request.JoinPinRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Created by parksuho on 2022/04/06.
 * Modified by parksuho on 2022/04/08.
 */
class PinControllerTest extends ControllerTest {
    private User host;
    private TokenDto hostTokenDto;
    private Long meetingPinId;
    private Long communityPinId;
    private User user;
    private TokenDto userTokenDto;

    @BeforeEach
    public void setUp() {
        host = userRepository.save(User.builder()
                .name("호스트 이름")
                .nickName("핀돌이")
                .resRedNumber("000302-3")
                .phoneNum("010-5678-5678")
                .age(22)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());
        hostTokenDto = userService.login(LoginUserRequest.builder().phoneNum("010-5678-5678").build());

        user = userRepository.save(User.builder()
                .name("임꺽정")
                .nickName("꺽정임")
                .resRedNumber("040606-3")
                .phoneNum("010-6543-4123")
                .age(22)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());
        userTokenDto = userService.login(LoginUserRequest.builder().phoneNum("010-6543-4123").build());

        MeetingPin meetingPin = MeetingPin.builder()
                .createUser(host).latitude(37.284365).longitude(127.044380)
                .dateTime(LocalDateTime.of(2022, 4, 12, 14, 0)).genderLimit(Gender.Male)
                .maxAge(100).minAge(24)
                .content("팔달관 모임").setLimit(30).category(MeetingPin.MeetingPinCategory.FOOD).build();
        meetingPin.addImage("사진1.jpg");
        meetingPinRepository.save(meetingPin);
        meetingPinId = meetingPin.getId();

        MeetingPinRequest request = MeetingPinRequest.builder()
                .requestMeetingPin(meetingPin).requestUser(host).state(State.Approved).content("CREATE").build();
        meetingPinRequestRepository.save(request);
        meetingPin.addRequest(request);

        MeetingPinParticipants build = MeetingPinParticipants.builder().meetingPin(meetingPin).user(host).build();
        meetingPinParticipantsRepository.save(build);
        meetingPin.addParticipant(build);

        CommunityPin communityPin = CommunityPin.builder()
                .createUser(host).title("사과대 모임").latitude(37.282209).longitude(127.046037)
                .startedAt(LocalDateTime.of(2022, 6, 11, 7, 0)).genderLimit(Gender.Female)
                .maxAge(29).minAge(20).content("사과대 모임입니다.").setLimit(30)
                .category(CommunityPin.CommunityPinCategory.APARTMENT)
                .communityPinType(CommunityPin.CommunityPinType.PERSONAL)
                .participationMethod(CommunityPin.ParticipationMethod.FREE).build();
        communityPin.updateImage("사진2.jpg");
        communityPinRepository.save(communityPin);
        communityPinId = communityPin.getId();

        CommunityPinRequest communityRequest = CommunityPinRequest.builder()
                .requestCommunityPin(communityPin).requestUser(host).state(State.Approved).content("CREATE").build();
        communityPinRequestRepository.save(communityRequest);
        communityPin.addRequest(communityRequest);

        CommunityPinParticipants build1 = CommunityPinParticipants.builder()
                .communityPin(communityPin).member(host).position(Position.Master).build();
        communityPinParticipantsRepository.save(build1);
        communityPin.addParticipant(build1);
    }

    @AfterEach
    public void setDown() {
    }

    @Test
    @Transactional
    void createMeetingPin() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(CreateMeetingPinRequest.builder()
                .latitude(37.283809).longitude(127.043746).category(MeetingPin.MeetingPinCategory.CULTURAL)
                .dateTime(LocalDateTime.of(2022, 5, 17, 9, 0)).genderLimit(Gender.Both)
                .maxAge(25).minAge(20).setLimit(30).content("Dong-guan moim").build());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", object.getBytes());
        //when
        ResultActions perform = mockMvc.perform(multipart("/pin/meeting")
                .file(new MockMultipartFile("files", UUID.randomUUID().toString() +".png", "image/png", "test".getBytes()))
                .file(request).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + hostTokenDto.getAccessToken()));
        //then
        perform.andDo(print())
                .andDo(document("meetingPin/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("호스트의 Access Token")),
                        requestParts(
                                partWithName("request").description("만남핀 정보"),
                                partWithName("files").description("만님핀 이미지들")
                        ),
                        requestPartBody("request"),
                        requestPartFields("request",
                                fieldWithPath("latitude").description("만님핀 위도"),
                                fieldWithPath("longitude").description("만님핀 경도"),
                                fieldWithPath("category").description("만님핀 카테고리"),
                                fieldWithPath("dateTime").description("만님핀 만남시간"),
                                fieldWithPath("genderLimit").description("만님핀 제한성별"),
                                fieldWithPath("maxAge").description("만님핀 최대나이"),
                                fieldWithPath("minAge").description("만님핀 최소나이"),
                                fieldWithPath("setLimit").description("만님핀 제한인원"),
                                fieldWithPath("content").description("만님핀 내용")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void createCommunityPin() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(CreateCommunityPinRequest.builder()
                .latitude(37.282967).longitude(127.043469).category(CommunityPin.CommunityPinCategory.ETC)
                .participationMethod(CommunityPin.ParticipationMethod.APPLICATION)
                .communityPinType(CommunityPin.CommunityPinType.ANONYMOUS).genderLimit(Gender.Both)
                .maxAge(25).minAge(25).setLimit(170).title("Woncheon-guan moim").content("See you Woncheon-guan").build());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", object.getBytes());
        //when
        ResultActions perform = mockMvc.perform(multipart("/pin/community")
                .file(new MockMultipartFile("file", UUID.randomUUID().toString() +".png", "image/png", "test".getBytes()))
                .file(request).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + hostTokenDto.getAccessToken()));
        //then
        perform.andDo(print())
                .andDo(document("communityPin/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("호스트의 Access Token")),
                        requestParts(
                                partWithName("request").description("커뮤니티핀 정보"),
                                partWithName("file").description("커뮤니티핀 이미지")
                        ),
                        requestPartBody("request"),
                        requestPartFields("request",
                                fieldWithPath("latitude").description("커뮤니티핀 위도"),
                                fieldWithPath("longitude").description("커뮤니티핀 경도"),
                                fieldWithPath("category").description("커뮤니티핀 카테고리"),
                                fieldWithPath("participationMethod").description("커뮤니티핀 가입방법"),
                                fieldWithPath("communityPinType").description("커뮤니티핀 종류"),
                                fieldWithPath("genderLimit").description("커뮤니티핀 제한성별"),
                                fieldWithPath("maxAge").description("커뮤니티핀 최대나이"),
                                fieldWithPath("minAge").description("커뮤니티핀 최소나이"),
                                fieldWithPath("setLimit").description("커뮤니티핀 제한인원"),
                                fieldWithPath("title").description("커뮤니티핀 제목"),
                                fieldWithPath("content").description("커뮤니티핀 내용")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void meetingPinInfo() throws Exception {
        //given
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders
                .get("/pin/meeting/{pinId}", meetingPinId)
                .header("Authorization", "Bearer " + userTokenDto.getAccessToken()));
        //then
        //when
        actions
                .andDo(print())
                .andDo(document("meetingPin/info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("유저의 Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("만남핀 번호")
                        ),
                        responseFields(
                                fieldWithPath("data.host").description("호스트"),
                                fieldWithPath("data.host.id").description("호스트 번호"),
                                fieldWithPath("data.host.nickName").description("호스트 닉네임"),
                                fieldWithPath("data.host.age").description("호스트 나이"),
                                fieldWithPath("data.host.gender").description("호스트 성별"),
                                fieldWithPath("data.host.profileImage").description("호스트 사진"),
                                fieldWithPath("data.category").description("만남핀 카테고리"),
                                fieldWithPath("data.content").description("만남핀 내용"),
                                fieldWithPath("data.images").description("만남핀 이미지들"),
                                fieldWithPath("data.createdAt").description("만남핀 생성일"),
                                fieldWithPath("data.location").description("만남핀 위치"),
                                fieldWithPath("data.meetingTime").description("만남핀 만남날짜"),
                                fieldWithPath("data.ageLimit").description("만남핀 나이제한"),
                                fieldWithPath("data.genderLimit").description("만남핀 성별제한"),
                                fieldWithPath("data.participants").description("만남핀 참가자들"),
                                fieldWithPath("data.participants.[].id").description("참가자 번호"),
                                fieldWithPath("data.participants.[].nickName").description("참가자 닉네임"),
                                fieldWithPath("data.participants.[].age").description("참가자 나이"),
                                fieldWithPath("data.participants.[].gender").description("참가자 성별"),
                                fieldWithPath("data.participants.[].profileImage").description("참가자 사진"),
                                fieldWithPath("data.participantsNum").description("만남핀 현재 참가자수"),
                                fieldWithPath("data.setLimit").description("만남핀 참가자 제한수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void communityPinInfo() throws Exception {
        //given
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders
                .get("/pin/community/{pinId}", communityPinId)
                .header("Authorization", "Bearer " + userTokenDto.getAccessToken()));
        //then
        //when
        actions
                .andDo(print())
                .andDo(document("communityPin/info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("유저의 Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("커뮤니티핀 번호")
                        ),
                        responseFields(
                                fieldWithPath("data.title").description("커뮤니티핀 제목"),
                                fieldWithPath("data.communityPinType").description("커뮤니티핀 종류"),
                                fieldWithPath("data.category").description("커뮤니티핀 카테고리"),
                                fieldWithPath("data.content").description("커뮤니티핀 내용"),
                                fieldWithPath("data.image").description("커뮤니티핀 이미지"),
                                fieldWithPath("data.participantsNum").description("커뮤니티핀 현재 참가자수"),
                                fieldWithPath("data.location").description("커뮤니티핀 장소"),
                                fieldWithPath("data.startedAt").description("커뮤니티핀 생성일"),
                                fieldWithPath("data.ageLimit").description("커뮤니티핀 나이제한"),
                                fieldWithPath("data.genderLimit").description("커뮤니티핀 제한성별"),
                                fieldWithPath("data.setLimit").description("커뮤니티핀 참가자 제한수"),
                                fieldWithPath("data.participants").description("커뮤니티핀 참가자들"),
                                fieldWithPath("data.participants.[].id").description("참가자 번호"),
                                fieldWithPath("data.participants.[].nickName").description("참가자 닉네임"),
                                fieldWithPath("data.participants.[].age").description("참가자 나이"),
                                fieldWithPath("data.participants.[].gender").description("참가자 성별"),
                                fieldWithPath("data.participants.[].profileImage").description("참가자 사진")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void joinMeetingPin() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(JoinPinRequest.builder().content("안녕하세요").build());
        //when
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders
                .post("/pin/meeting/{pinId}/join", meetingPinId)
                .header("Authorization", "Bearer " + userTokenDto.getAccessToken())
                .content(object).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        //then
        perform.andDo(print())
                .andDo(document("meetingPin/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("참여자의 Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("만남핀 번호")
                        ),
                        requestFields(
                                fieldWithPath("content").description("만남핀 신청내용")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void joinCommunityPin() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(JoinPinRequest.builder().content("커뮤니티핀 가입해요").build());
        //when
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders
                .post("/pin/community/{pinId}/join", communityPinId)
                .header("Authorization", "Bearer " + userTokenDto.getAccessToken())
                .content(object).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        //then
        perform.andDo(print())
                .andDo(document("communityPin/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("참여자의 Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("커뮤니티핀 번호")
                        ),
                        requestFields(
                                fieldWithPath("content").description("커뮤니티핀 신청내용")
                        )
                ))
                .andExpect(status().isCreated());
    }
}