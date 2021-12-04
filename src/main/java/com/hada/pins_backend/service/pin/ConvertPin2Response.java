package com.hada.pins_backend.service.pin;

import com.hada.pins_backend.domain.Gender;
import com.hada.pins_backend.domain.meetingPin.UserAndMeetingPin;
import com.hada.pins_backend.domain.user.User;
import com.hada.pins_backend.dto.pin.response.MeetingPinResponse;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by bangjinhyuk on 2021/12/04.
 */
public class ConvertPin2Response {
    //위도, 경도 to 주소
    public static String GpsToAddress(Double latitude, Double longitude, String googleKey){
        GpsToAddress gps = null;
        try {
            gps = new GpsToAddress(latitude, longitude, googleKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gps != null) {
            return gps.getAddress();
        }else return "주소 오류";
    }
    //만남 날짜 to 상세 페이지 형식
    public static String localDateTime2String(LocalDateTime localDateTime){
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
    //~분전,~시전,~일전 상세 페이지 양식
    public static String createdAt2String(LocalDateTime localDateTime){
        LocalDateTime now = LocalDateTime.now();
        StringBuilder createAtSb = new StringBuilder();

        if(ChronoUnit.DAYS.between(localDateTime, now)<1){
            if(ChronoUnit.HOURS.between(localDateTime, now)<1) createAtSb.append(ChronoUnit.MINUTES.between(localDateTime, now)).append("분 전");
            else createAtSb.append(ChronoUnit.HOURS.between(localDateTime, now)).append("시간 전");
        }else{
            createAtSb.append(ChronoUnit.DAYS.between(localDateTime, now)).append("일 전");
        }
        return createAtSb.toString();
    }

    public static MeetingPinResponse.CreateDetail user2CreateDetail(User createUser){
        String userdetail = createUser.getAge() + "세 " +
                Gender.convert2Korean(createUser.getGender());
        return new MeetingPinResponse.CreateDetail(
                createUser.getId(),
                createUser.getNickName(),
                userdetail,
                createUser.getProfileImage()
        );
    }

    public static List<MeetingPinResponse.ParticipantDetail> user2ParticipantDetail(List<UserAndMeetingPin> userAndMeetingPins){
        List<MeetingPinResponse.ParticipantDetail> participantDetailList = new ArrayList<>();
        for (UserAndMeetingPin userAndMeetingPin : userAndMeetingPins) {
            User tmpUser = userAndMeetingPin.getMember();
            String detail = tmpUser.getAge() + "세 " +
                    Gender.convert2Korean(tmpUser.getGender());
            participantDetailList.add(
                    new MeetingPinResponse.ParticipantDetail(
                            tmpUser.getId(),
                            tmpUser.getNickName(),
                            detail,
                            tmpUser.getProfileImage()
                    )
            );
        }
        return participantDetailList;

    }

}
