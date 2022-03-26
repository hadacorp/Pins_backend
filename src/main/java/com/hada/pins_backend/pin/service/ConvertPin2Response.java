package com.hada.pins_backend.pin.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by bangjinhyuk on 2022/01/29.
 */
public class ConvertPin2Response {
    //위도, 경도 to 주소
//    public String GpsToAddress(Double latitude, Double longitude, String googleKey){
//        GpsToAddress gps = null;
//        try {
//            gps = new GpsToAddress(latitude, longitude, googleKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (gps != null) {
//            return gps.getAddress();
//        }else return "주소 오류";
//    }
    //만남 날짜 to 상세 페이지 형식
    public String localDateTime2String(LocalDateTime localDateTime){
        StringTokenizer dayTokens = new StringTokenizer(localDateTime.format(DateTimeFormatter.ofPattern("MM월 dd일 -HH")),"-");
        StringBuilder day = new StringBuilder();
        day.append(dayTokens.nextToken());
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        day.append("(").append(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)).append(") ");

        int hour = Integer.parseInt(dayTokens.nextToken());
        if(hour>12) day.append("오후 ").append(hour-12).append("시");
        else day.append("오전 ").append(hour).append("시");
        return day.toString();
    }

//    public MeetingPinCardViewResponse.CreateDetail user2CreateDetail(User createUser){
//        String userdetail = createUser.getAge() + "세 " +
//                Gender.convert2Korean(createUser.getGender());
//        return new MeetingPinCardViewResponse.CreateDetail(
//                createUser.getId(),
//                createUser.getNickName(),
//                userdetail,
//                createUser.getProfileImage()
//        );
//    }

//    public List<MeetingPinResponse.ParticipantDetail> user2ParticipantDetail(List<UserAndMeetingPin> userAndMeetingPins){
//        List<MeetingPinResponse.ParticipantDetail> participantDetailList = new ArrayList<>();
//        for (UserAndMeetingPin userAndMeetingPin : userAndMeetingPins) {
//            User tmpUser = userAndMeetingPin.getMember();
//            String detail = tmpUser.getAge() + "세 " +
//                    Gender.convert2Korean(tmpUser.getGender());
//            participantDetailList.add(
//                    new MeetingPinResponse.ParticipantDetail(
//                            tmpUser.getId(),
//                            tmpUser.getNickName(),
//                            detail,
//                            tmpUser.getProfileImage()
//                    )
//            );
//        }
//        return participantDetailList;
//    }

}