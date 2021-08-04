package com.example.pins_backend.domain.communityPost;

import com.example.pins_backend.domain.meetingPin.MeetingPin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostRepository extends JpaRepository<CommunityPost,Long> {

}
