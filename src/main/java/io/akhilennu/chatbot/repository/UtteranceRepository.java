package io.akhilennu.chatbot.repository;

import io.akhilennu.chatbot.domain.Utterance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Utterance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtteranceRepository extends JpaRepository<Utterance, Long> {}
