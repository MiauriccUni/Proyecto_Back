package com.mahou.mahouback.logic.entity.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForumPostRepository extends JpaRepository<ForumPost, Integer> {

    List<ForumPost> findByIsPublicTrueOrderByPublishedAtDesc();

    List<ForumPost> findByAuthorId(Integer authorId);

    Optional<ForumPost> findByStoryId(Integer storyId);

    List<ForumPost> findByIsPublicTrueOrAuthorIdOrderByPublishedAtDesc(Integer authorId);
}
