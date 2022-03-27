package com.hada.pins_backend.documentation;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

/**
 * Created by bangjinhyuk on 2022/03/27.
 */

public class HomeDocumentation {
    public static RestDocumentationResultHandler getPins() {
        return document("v1/home/pin",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("회원 Access Token")),
                requestParameters(
                        parameterWithName("latitude").description("위도 : Double"),
                        parameterWithName("longitude").description("경도 : Double"),
                        parameterWithName("meetDate").description("만남핀 날짜 리스트 : LocalDate List yyyy-MM-dd").optional(),
                        parameterWithName("startMeetTime").description("만남핀 조회 시작 시간 : LocalTime HH:mm:ss").optional(),
                        parameterWithName("endMeetTime").description("만남핀 조회 끝 시간 : LocalTime HH:mm:ss").optional(),
                        parameterWithName("minAge").description("최소 나이 : 최소 20").optional(),
                        parameterWithName("maxAge").description("최대 나이 : 최대 100").optional(),
                        parameterWithName("keyword").description("키워드").optional(),
                        parameterWithName("meetGender").description("성별 : Male, Female, Both").optional(),
                        parameterWithName("meetingPinCategory").description("만남핀 카테고리 리스트 : FRIENDSHIP,\n" +
                                "        WALK,\n" +
                                "        PET,\n" +
                                "        FOOD,\n" +
                                "        CULTURAL,\n" +
                                "        GAME,\n" +
                                "        EXERCISE,\n" +
                                "        MOUNTAIN,\n" +
                                "        STUDY,\n" +
                                "        JOURNEY,\n" +
                                "        ETC").optional(),
                        parameterWithName("communityPinCategory").description("커뮤니티핀 카테고리 리스트 : FRIENDSHIP,\n" +
                                "        WALK,\n" +
                                "        PET,\n" +
                                "        FOOD,\n" +
                                "        CULTURAL,\n" +
                                "        GAME,\n" +
                                "        EXERCISE,\n" +
                                "        MOUNTAIN,\n" +
                                "        STUDY,\n" +
                                "        JOURNEY,\n" +
                                "        SCHOOL,\n" +
                                "        APARTMENT,\n" +
                                "        ETC").optional(),
                        parameterWithName("storyPinCategory").description("이야기핀 카테고리 리스트 : CURIOUS,\n" +
                                "        REVIEW,\n" +
                                "        TOWN,\n" +
                                "        PET,\n" +
                                "        HELP,\n" +
                                "        ACCIDENT,\n" +
                                "        LOST,\n" +
                                "        CHAT,\n" +
                                "        ETC").optional()
                ),
                responseFields(
                        fieldWithPath("data.storyPin[].id").description("이야기핀 id"),
                        fieldWithPath("data.storyPin[].createUser.id").description("생성 유저 id"),
                        fieldWithPath("data.storyPin[].createUser.nickName").description("생성 유저 닉네임"),
                        fieldWithPath("data.storyPin[].createUser.age").description("생성 유저 나이"),
                        fieldWithPath("data.storyPin[].createUser.gender").description("생성 유저 성별"),
                        fieldWithPath("data.storyPin[].createUser.profileImage").description("생성 유 이미지"),
                        fieldWithPath("data.storyPin[].latitude").description("이야기핀 위도"),
                        fieldWithPath("data.storyPin[].longitude").description("이야기핀 경도"),
                        fieldWithPath("data.storyPin[].createAt").description("이야기핀 생성 일자"),
                        fieldWithPath("data.storyPin[].category").description("이야기핀 카테고리"),
                        fieldWithPath("data.storyPin[].content").description("이야기핀 내용"),
                        fieldWithPath("data.storyPin[].images").description("이야기핀 이미지"),
                        fieldWithPath("data.communityPin[].id").description("커뮤니티핀 id"),
                        fieldWithPath("data.communityPin[].createUser.id").description("생성 유저 id"),
                        fieldWithPath("data.communityPin[].createUser.nickName").description("생성 유저 닉네임"),
                        fieldWithPath("data.communityPin[].createUser.age").description("생성 유저 나이"),
                        fieldWithPath("data.communityPin[].createUser.gender").description("생성 유저 성별"),
                        fieldWithPath("data.communityPin[].createUser.profileImage").description("생성 유저 이미지"),
                        fieldWithPath("data.communityPin[].latitude").description("커뮤니티핀 위도"),
                        fieldWithPath("data.communityPin[].longitude").description("커뮤니티핀 경도"),
                        fieldWithPath("data.communityPin[].startedAt").description("커뮤니티핀 시작 일자"),
                        fieldWithPath("data.communityPin[].category").description("커뮤니티핀 카테고리"),
                        fieldWithPath("data.communityPin[].content").description("커뮤니티핀 내용"),
                        fieldWithPath("data.communityPin[].images").description("커뮤니티핀 이미지"),
                        fieldWithPath("data.communityPin[].participantNum").description("커뮤니티핀 참여 인원"),
                        fieldWithPath("data.meetingPin[].id").description("만남핀 id"),
                        fieldWithPath("data.meetingPin[].createUser.id").description("생성 유저 id"),
                        fieldWithPath("data.meetingPin[].createUser.nickName").description("생성 유저 닉네임"),
                        fieldWithPath("data.meetingPin[].createUser.age").description("생성 유저 나이"),
                        fieldWithPath("data.meetingPin[].createUser.gender").description("생성 유저 성별"),
                        fieldWithPath("data.meetingPin[].createUser.profileImage").description("생성 유 이미지"),
                        fieldWithPath("data.meetingPin[].latitude").description("만남핀 위도"),
                        fieldWithPath("data.meetingPin[].longitude").description("만남핀 경도"),
                        fieldWithPath("data.meetingPin[].address").description("만남핀 주소"),
                        fieldWithPath("data.meetingPin[].dateTime").description("만남 시간"),
                        fieldWithPath("data.meetingPin[].category").description("만남핀 카테고리"),
                        fieldWithPath("data.meetingPin[].content").description("만남핀 내용"),
                        fieldWithPath("data.meetingPin[].images").description("만남핀 이미지"),
                        fieldWithPath("data.meetingPin[].setLimit").description("만남핀 인원 제한"),
                        fieldWithPath("data.meetingPin[].participantNum").description("만남핀 참여 인원수")
                )
        );
    }
}
