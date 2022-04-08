package com.hada.pins_backend;

/*
 * Created by parksuho on 2022/04/06.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.account.repository.RefreshTokenRepository;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.account.service.UserService;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinParticipantsRepository;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinRepository;
import com.hada.pins_backend.pin.repository.communityPin.CommunityPinRequestRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinParticipantsRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRepository;
import com.hada.pins_backend.pin.repository.meetingPin.MeetingPinRequestRepository;
import com.hada.pins_backend.pin.repository.storyPin.StoryPinRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/*
 * Created by parksuho on 2022/04/06.
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;
    @Autowired
    protected UserService userService;
    @Autowired
    protected MeetingPinRepository meetingPinRepository;
    @Autowired
    protected MeetingPinRequestRepository meetingPinRequestRepository;
    @Autowired
    protected MeetingPinParticipantsRepository meetingPinParticipantsRepository;
    @Autowired
    protected CommunityPinRepository communityPinRepository;
    @Autowired
    protected CommunityPinRequestRepository communityPinRequestRepository;
    @Autowired
    protected CommunityPinParticipantsRepository communityPinParticipantsRepository;
    @Autowired
    protected StoryPinRepository storyPinRepository;
}
