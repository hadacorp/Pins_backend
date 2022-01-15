package com.hada.pins_backend.domain.communityPost;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bangjinhyuk on 2021/08/22.
 */
public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike,Long> {
    List<CommunityPostLike> findCommunityPostLikesByCommunityPost_Id (Long communityPostId);

}
