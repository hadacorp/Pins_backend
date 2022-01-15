package com.hada.pins_backend.domain.storyPin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryPinCommentRepository extends JpaRepository<StoryPinComment,Long> {
    List<StoryPinComment> findStoryPinCommentsByStoryPin_Id (Long storyPinLikeId);
}
