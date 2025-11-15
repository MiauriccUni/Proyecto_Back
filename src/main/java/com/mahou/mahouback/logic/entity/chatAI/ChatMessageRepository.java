package com.mahou.mahouback.logic.entity.chatAI;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByHistoriaIdOrderByCreatedAtAsc(Integer historiaId);
    List<ChatMessage> findByUserIdOrderByCreatedAtDesc(Long userId);
}
