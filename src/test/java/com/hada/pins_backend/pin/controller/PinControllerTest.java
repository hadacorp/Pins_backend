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
                .name("????????? ??????")
                .nickName("?????????")
                .resRedNumber("000302-3")
                .phoneNum("010-5678-5678")
                .age(22)
                .gender(Gender.Male)
                .profileImage("test.png")
                .role("ROLE_USER")
                .build());
        hostTokenDto = userService.login(LoginUserRequest.builder().phoneNum("010-5678-5678").build());

        user = userRepository.save(User.builder()
                .name("?????????")
                .nickName("?????????")
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
                .content("????????? ??????").setLimit(30).category(MeetingPin.MeetingPinCategory.FOOD).build();
        meetingPin.addImage("??????1.jpg");
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
                .createUser(host).title("????????? ??????").latitude(37.282209).longitude(127.046037)
                .startedAt(LocalDateTime.of(2022, 6, 11, 7, 0)).genderLimit(Gender.Female)
                .maxAge(29).minAge(20).content("????????? ???????????????.").setLimit(30)
                .category(CommunityPin.CommunityPinCategory.APARTMENT)
                .communityPinType(CommunityPin.CommunityPinType.PERSONAL)
                .participationMethod(CommunityPin.ParticipationMethod.FREE).build();
        communityPin.updateImage("??????2.jpg");
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
                        requestHeaders(headerWithName("Authorization").description("???????????? Access Token")),
                        requestParts(
                                partWithName("request").description("????????? ??????"),
                                partWithName("files").description("????????? ????????????")
                        ),
                        requestPartBody("request"),
                        requestPartFields("request",
                                fieldWithPath("latitude").description("????????? ??????"),
                                fieldWithPath("longitude").description("????????? ??????"),
                                fieldWithPath("category").description("????????? ????????????"),
                                fieldWithPath("dateTime").description("????????? ????????????"),
                                fieldWithPath("genderLimit").description("????????? ????????????"),
                                fieldWithPath("maxAge").description("????????? ????????????"),
                                fieldWithPath("minAge").description("????????? ????????????"),
                                fieldWithPath("setLimit").description("????????? ????????????"),
                                fieldWithPath("content").description("????????? ??????")
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
                        requestHeaders(headerWithName("Authorization").description("???????????? Access Token")),
                        requestParts(
                                partWithName("request").description("??????????????? ??????"),
                                partWithName("file").description("??????????????? ?????????")
                        ),
                        requestPartBody("request"),
                        requestPartFields("request",
                                fieldWithPath("latitude").description("??????????????? ??????"),
                                fieldWithPath("longitude").description("??????????????? ??????"),
                                fieldWithPath("category").description("??????????????? ????????????"),
                                fieldWithPath("participationMethod").description("??????????????? ????????????"),
                                fieldWithPath("communityPinType").description("??????????????? ??????"),
                                fieldWithPath("genderLimit").description("??????????????? ????????????"),
                                fieldWithPath("maxAge").description("??????????????? ????????????"),
                                fieldWithPath("minAge").description("??????????????? ????????????"),
                                fieldWithPath("setLimit").description("??????????????? ????????????"),
                                fieldWithPath("title").description("??????????????? ??????"),
                                fieldWithPath("content").description("??????????????? ??????")
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
                        requestHeaders(headerWithName("Authorization").description("????????? Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("data.host").description("?????????"),
                                fieldWithPath("data.host.id").description("????????? ??????"),
                                fieldWithPath("data.host.nickName").description("????????? ?????????"),
                                fieldWithPath("data.host.age").description("????????? ??????"),
                                fieldWithPath("data.host.gender").description("????????? ??????"),
                                fieldWithPath("data.host.profileImage").description("????????? ??????"),
                                fieldWithPath("data.category").description("????????? ????????????"),
                                fieldWithPath("data.content").description("????????? ??????"),
                                fieldWithPath("data.images").description("????????? ????????????"),
                                fieldWithPath("data.createdAt").description("????????? ?????????"),
                                fieldWithPath("data.location").description("????????? ??????"),
                                fieldWithPath("data.meetingTime").description("????????? ????????????"),
                                fieldWithPath("data.ageLimit").description("????????? ????????????"),
                                fieldWithPath("data.genderLimit").description("????????? ????????????"),
                                fieldWithPath("data.participants").description("????????? ????????????"),
                                fieldWithPath("data.participants.[].id").description("????????? ??????"),
                                fieldWithPath("data.participants.[].nickName").description("????????? ?????????"),
                                fieldWithPath("data.participants.[].age").description("????????? ??????"),
                                fieldWithPath("data.participants.[].gender").description("????????? ??????"),
                                fieldWithPath("data.participants.[].profileImage").description("????????? ??????"),
                                fieldWithPath("data.participantsNum").description("????????? ?????? ????????????"),
                                fieldWithPath("data.setLimit").description("????????? ????????? ?????????")
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
                        requestHeaders(headerWithName("Authorization").description("????????? Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("??????????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("data.title").description("??????????????? ??????"),
                                fieldWithPath("data.communityPinType").description("??????????????? ??????"),
                                fieldWithPath("data.category").description("??????????????? ????????????"),
                                fieldWithPath("data.content").description("??????????????? ??????"),
                                fieldWithPath("data.image").description("??????????????? ?????????"),
                                fieldWithPath("data.participantsNum").description("??????????????? ?????? ????????????"),
                                fieldWithPath("data.location").description("??????????????? ??????"),
                                fieldWithPath("data.startedAt").description("??????????????? ?????????"),
                                fieldWithPath("data.ageLimit").description("??????????????? ????????????"),
                                fieldWithPath("data.genderLimit").description("??????????????? ????????????"),
                                fieldWithPath("data.setLimit").description("??????????????? ????????? ?????????"),
                                fieldWithPath("data.participants").description("??????????????? ????????????"),
                                fieldWithPath("data.participants.[].id").description("????????? ??????"),
                                fieldWithPath("data.participants.[].nickName").description("????????? ?????????"),
                                fieldWithPath("data.participants.[].age").description("????????? ??????"),
                                fieldWithPath("data.participants.[].gender").description("????????? ??????"),
                                fieldWithPath("data.participants.[].profileImage").description("????????? ??????")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void joinMeetingPin() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(JoinPinRequest.builder().content("???????????????").build());
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
                        requestHeaders(headerWithName("Authorization").description("???????????? Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("content").description("????????? ????????????")
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void joinCommunityPin() throws Exception {
        //given
        String object = objectMapper.writeValueAsString(JoinPinRequest.builder().content("??????????????? ????????????").build());
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
                        requestHeaders(headerWithName("Authorization").description("???????????? Access Token")),
                        pathParameters(
                                parameterWithName("pinId").description("??????????????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("content").description("??????????????? ????????????")
                        )
                ))
                .andExpect(status().isCreated());
    }
}