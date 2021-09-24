package com.hada.pins_backend.dto.home;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

/**
 * Created by bangjinhyuk on 2021/09/24.
 */
@Getter
@ToString
public class FilterData {
    private String meetingPinCategory;
    private String meetDate;
    private String meetTime;
    private String meetGender;
    private String meetAge;
    private String communityPinCategory;
    private String storyPinCategory;

    @Builder
    public FilterData(String meetingPinCategory, String meetDate, String meetTime, String meetGender, String meetAge, String communityPinCategory, String storyPinCategory) {
        this.meetingPinCategory = meetingPinCategory;
        this.meetDate = meetDate;
        this.meetTime = meetTime;
        this.meetGender = meetGender;
        this.meetAge = meetAge;
        this.communityPinCategory = communityPinCategory;
        this.storyPinCategory = storyPinCategory;
    }

    public String getRenameGender() {
        if(this.meetGender.equals("all")) return "Male-Female";
        else return this.meetGender;
    }
    public String getRenameDate() {
        LocalDate now = LocalDate.now();
        StringBuilder date = new StringBuilder();
        if(this.meetDate.equals("all")){
            for(int i =0;i<8;i++){
                date.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("-");
                now = now.plusDays(1);
            }
            return date.toString();
        }else{
            for(int i =0;i<8;i++){
                if(this.meetDate.contains(String.valueOf(i))){
                    date.append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("-");
                }
                now = now.plusDays(1);
            }
            return date.toString();
        }
    }

    public int getRenameMinAge() {
        if(this.meetAge.equals("all")){
            return  20;
        }else{
            StringTokenizer st = new StringTokenizer(meetAge,"-");
            return Integer.parseInt(st.nextToken());
        }
    }

    public int getRenameMaxAge() {
        if(this.meetAge.equals("all")){
            return 100;
        }else{
            StringTokenizer st = new StringTokenizer(meetAge,"-");
            st.nextToken();
            return Integer.parseInt(st.nextToken());
        }
    }
    public int getRenameMinTime(){
        if(this.meetTime.equals("all")){
            return 0;
        }else{
            StringTokenizer st = new StringTokenizer(meetTime,"-");
            return Integer.parseInt(st.nextToken());
        }
    }

    public int getRenameMaxTime(){
        if(this.meetTime.equals("all")){
            return 24;
        }else{
            StringTokenizer st = new StringTokenizer(meetTime,"-");
            st.nextToken();
            return Integer.parseInt(st.nextToken());
        }
    }
    public String getRenameMeetingPinCategory(){
        if(this.meetingPinCategory.equals("all")) {
            return "대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-거래/나눔-기타";
        }
        else return this.meetingPinCategory;
    }

    public String getRenameCommunityPinCategory(){
        if(this.communityPinCategory.equals("all")) {
            return "학교/동창-아파트/이웃-대화/친목-산책/반려동물-맛집탐방-영화/문화생활-게임/오락-스포츠/운동-등산/캠핑-스터디/독서-여행/드라이브-기타";
        }
        else return this.communityPinCategory;
    }
    public String getRenameStoryPinCategory(){
        if(this.storyPinCategory.equals("all")) {
            return "#궁금해요#장소리뷰#동네꿀팁#반려동물#취미생활#도와줘요#사건시고#분실/실종#잡담";
        }
        else return this.storyPinCategory;
    }

}
