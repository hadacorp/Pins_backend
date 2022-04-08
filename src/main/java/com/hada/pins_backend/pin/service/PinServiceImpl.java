package com.hada.pins_backend.pin.service;

import com.hada.pins_backend.account.exception.CUserNotFoundException;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.model.enumable.Gender;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.handler.FileHandler;
import com.hada.pins_backend.handler.S3Uploader;
import com.hada.pins_backend.model.FileFolderName;
import com.hada.pins_backend.pin.exception.CCommunityImageException;
import com.hada.pins_backend.pin.exception.CDateException;
import com.hada.pins_backend.pin.exception.CPinParticipantsLimitException;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPin;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinParticipants;
import com.hada.pins_backend.pin.model.entity.communityPin.CommunityPinRequest;
import com.hada.pins_backend.pin.model.entity.dto.SimpleUser;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPin;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinParticipants;
import com.hada.pins_backend.pin.model.entity.meetingPin.MeetingPinRequest;
import com.hada.pins_backend.pin.model.enumable.Position;
import com.hada.pins_backend.pin.model.enumable.State;
import com.hada.pins_backend.pin.model.request.CreateCommunityPinRequest;
import com.hada.pins_backend.pin.model.request.CreateMeetingPinRequest;
import com.hada.pins_backend.pin.model.request.JoinPinRequest;
import com.hada.pins_backend.pin.model.response.CommunityPinDto;
import com.hada.pins_backend.pin.model.response.MeetingPinDto;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinParticipantsRepository;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinRepository;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinRequestRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinParticipantsRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Created by parksuho on 2022/04/05.
 * Modified by parksuho on 2022/04/08.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {
    private final UserRepository userRepository;
    private final MeetingPinRepository meetingPinRepository;
    private final CommunityPinRepository communityPinRepository;
    private final MeetingPinRequestRepository meetingPinRequestRepository;
    private final CommunityPinRequestRepository communityPinRequestRepository;
    private final MeetingPinParticipantsRepository meetingPinParticipantsRepository;
    private final CommunityPinParticipantsRepository communityPinParticipantsRepository;
    private final GpsToAddress gpsToAddress;
    private final FileHandler fileHandler;
    private final S3Uploader s3Uploader;

    private String createAt(LocalDateTime createdAt, LocalDateTime now) {
        if (createdAt.isAfter(now)) throw new CDateException();
        long day = ChronoUnit.DAYS.between(createdAt, now);
        if (day > 0) return day + "일전";

        long hour = ChronoUnit.HOURS.between(createdAt, now);
        if (hour > 0)  return hour + "시간 전";

        long minute = ChronoUnit.MINUTES.between(createdAt, now);
        return minute + "분전";
    }

    private String location(Double latitude, Double longitude) {
        return gpsToAddress.getAddress(latitude, longitude);
    }

    private String meetingTime(LocalDateTime now, LocalDateTime dateTime) {
        if (now.isAfter(dateTime)) { // 지난 날짜면
            return dateTime.getYear() + "년 " + dateTime.getMonthValue() + "월 " + dateTime.getDayOfMonth() + "일";
        }
        int hour = dateTime.getHour();
        String time;
        if (hour == 12) time = "낮 12시";
        else if (hour == 0) time = "밤 12시";
        else if (hour < 12) time = "오전 " + hour + "시";
        else time = "오후 " + (hour - 12) + "시";

        long day = ChronoUnit.DAYS.between(now, dateTime);
        if (day == 0) return "오늘 " + time;
        else if (day == 1) return "내일 " + time;

        LocalDateTime monday = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime nextMonday = now.with(DayOfWeek.SUNDAY).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        if(ChronoUnit.DAYS.between(monday, dateTime) < 0) throw new CDateException();
        if(ChronoUnit.DAYS.between(monday, dateTime) <= 6)
            return "이번주 " + dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN) + " " + time;
        else if(ChronoUnit.DAYS.between(nextMonday, dateTime) <= 6)
            return "다음주 " + dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN) + " " + time;
        else return dateTime.getYear() + "년 " + dateTime.getMonthValue() + "월 " + dateTime.getDayOfMonth() + "일 " + time;
    }

    private String ageLimit(Integer maxAge, Integer minAge) {
        if (minAge == null && maxAge == null) {
            return "나이 무관";
        } else if (maxAge == 100) {
            return minAge + "살 이상";
        } else if (minAge == 20) {
            return maxAge + "살 까지";
        } else {
            return minAge + "살 ~ " + maxAge + "살";
        }
    }

    @Transactional
    public void createMeetingPin(Long userId, CreateMeetingPinRequest request, List<MultipartFile> files) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

        MeetingPin pin = MeetingPin.builder().createUser(user).latitude(request.getLatitude()).longitude(request.getLongitude())
                .dateTime(request.getDateTime()).genderLimit(request.getGenderLimit())
                .maxAge(request.getMaxAge()).minAge(request.getMinAge()).content(request.getContent())
                .setLimit(request.getSetLimit()).category(request.getCategory()).build();
        meetingPinRepository.save(pin);

        List<String> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String meetingImagePath = fileHandler.parseFileInfo(file, pin.getId().toString(), FileFolderName.meetingImage);
            String meetingImage = s3Uploader.upload(file, meetingImagePath);
            images.add(meetingImage);
        }
        pin.updateImages(images);

        MeetingPinRequest build = MeetingPinRequest.builder().requestMeetingPin(pin).requestUser(user)
                .state(State.Approved).content("CREATE").build();
        meetingPinRequestRepository.save(build);
        pin.addRequest(build);

        MeetingPinParticipants host = MeetingPinParticipants.builder().user(user).meetingPin(pin).build();
        meetingPinParticipantsRepository.save(host);
        pin.addParticipant(host);
    }

    @Transactional
    public void createCommunityPin(Long userId, CreateCommunityPinRequest request, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);

        if(file.isEmpty()) throw new CCommunityImageException();
        
        CommunityPin pin = CommunityPin.builder().createUser(user).title(request.getTitle())
                .latitude(request.getLatitude()).longitude(request.getLongitude()).startedAt(LocalDateTime.now())
                .genderLimit(request.getGenderLimit()).maxAge(request.getMaxAge()).minAge(request.getMinAge())
                .content(request.getContent())
                .setLimit(request.getSetLimit()).category(request.getCategory())
                .communityPinType(request.getCommunityPinType()).participationMethod(request.getParticipationMethod()).build();
        communityPinRepository.save(pin);

        String communityImagePath = fileHandler.parseFileInfo(file, pin.getId().toString(), FileFolderName.communityImage);
        String communityImage = s3Uploader.upload(file, communityImagePath);
        pin.updateImage(communityImage);

        CommunityPinRequest build = CommunityPinRequest.builder().requestCommunityPin(pin).requestUser(user)
                .state(State.Approved).content("CREATE").build();
        communityPinRequestRepository.save(build);
        pin.addRequest(build);

        CommunityPinParticipants host = CommunityPinParticipants.builder()
                .member(user).communityPin(pin).position(Position.Master).build();
        communityPinParticipantsRepository.save(host);
        pin.addParticipant(host);
    }

    @Transactional(readOnly = true)
    public MeetingPinDto meetingPinInfo(Long pinId) {
        MeetingPin pin = meetingPinRepository.findById(pinId).orElseThrow();
        User createUser = pin.getCreateUser();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = pin.getCreatedAt();
        String pinCreateAt = createAt(createdAt, now);
        String location = location(pin.getLatitude(), pin.getLongitude());
        LocalDateTime dateTime = pin.getDateTime();
        String meetingTime = meetingTime(now, dateTime);
        String ageLimit = ageLimit(pin.getMaxAge(), pin.getMinAge());
        String genderLimit = Gender.convert2Korean(pin.getGenderLimit());

        List<MeetingPinParticipants> participants = pin.getMeetingPinParticipants();
        List<SimpleUser> users = new ArrayList<>();
        for (MeetingPinParticipants mpp : participants) {
            users.add(SimpleUser.of(mpp.getUser()));
        }
        Integer participantsNum = pin.getMeetingPinParticipants().size();

        return MeetingPinDto.builder().host(SimpleUser.of(createUser))
                .category(pin.getCategory()).content(pin.getContent()).images(pin.getImages())
                .createdAt(pinCreateAt).location(location).meetingTime(meetingTime).ageLimit(ageLimit).genderLimit(genderLimit)
                .participants(users).participantsNum(participantsNum).setLimit(pin.getSetLimit()).build();
    }

    @Transactional(readOnly = true)
    public CommunityPinDto communityPinInfo(Long pinId) {
        CommunityPin pin = communityPinRepository.findById(pinId).orElseThrow();

        String location = location(pin.getLatitude(), pin.getLongitude());
        LocalDateTime startTime = pin.getStartedAt();
        String startedAt = startTime.getYear() + "년 " + startTime.getMonthValue() + "월 " + startTime.getDayOfMonth() + "일부터";
        String ageLimit = ageLimit(pin.getMaxAge(), pin.getMinAge());
        String genderLimit = Gender.convert2Korean(pin.getGenderLimit());

        List<CommunityPinParticipants> participants = pin.getCommunityPinParticipants();
        List<SimpleUser> users = new ArrayList<>();
        for (CommunityPinParticipants mpp : participants) {
            users.add(SimpleUser.of(mpp.getMember()));
        }
        Integer participantsNum = pin.getCommunityPinParticipants().size();

        return CommunityPinDto.builder().title(pin.getTitle())
                .communityPinType(pin.getCommunityPinType()).category(pin.getCategory())
                .content(pin.getContent()).image(pin.getImage()).participantsNum(participantsNum)
                .location(location).startedAt(startedAt).ageLimit(ageLimit).genderLimit(genderLimit)
                .setLimit(pin.getSetLimit()).participants(users).build();
    }

    @Transactional
    public void joinMeetingPin(Long userId, Long pinId, JoinPinRequest request) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        MeetingPin meetingPin = meetingPinRepository.findById(pinId).orElseThrow();
//        if(이미 신청한 핀이면)
        if(meetingPin.checkLimit()) throw new CPinParticipantsLimitException();
        MeetingPinRequest build = MeetingPinRequest.builder().requestMeetingPin(meetingPin).requestUser(user)
                .state(State.Requested).content(request.getContent()).build();
        meetingPinRequestRepository.save(build);
        meetingPin.addRequest(build);
    }

    @Transactional
    public void joinCommunityPin(Long userId, Long pinId, JoinPinRequest request) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        CommunityPin communityPin = communityPinRepository.findById(pinId).orElseThrow();
//        if(이미 신청한 핀이면)
        if(communityPin.getCommunityPinParticipants().size() == communityPin.getSetLimit()) throw new CPinParticipantsLimitException();
        CommunityPinRequest build = CommunityPinRequest.builder().requestCommunityPin(communityPin).requestUser(user)
                .state(State.Requested).content(request.getContent()).build();
        communityPinRequestRepository.save(build);
    }
}
