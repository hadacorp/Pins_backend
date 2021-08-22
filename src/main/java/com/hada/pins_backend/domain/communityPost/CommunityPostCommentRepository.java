package com.hada.pins_backend.domain.communityPost;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostCommentRepository extends JpaRepository<CommunityPostComment,Long> {

    List<CommunityPostComment> findCommunityPostCommentsByCommunityPost_Id (Long communityPostId);

}
