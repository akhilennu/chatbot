package io.akhilennu.chatbot.repository;

import io.akhilennu.chatbot.domain.BotTraining;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BotTraining entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BotTrainingRepository extends JpaRepository<BotTraining, Long> {}
