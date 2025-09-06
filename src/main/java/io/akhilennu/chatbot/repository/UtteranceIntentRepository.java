package io.akhilennu.chatbot.repository;

import io.akhilennu.chatbot.domain.UtteranceIntent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UtteranceIntent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtteranceIntentRepository extends JpaRepository<UtteranceIntent, Long> {}
