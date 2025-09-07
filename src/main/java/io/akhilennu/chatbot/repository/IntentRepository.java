package io.akhilennu.chatbot.repository;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.service.dto.IntentDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Intent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntentRepository extends JpaRepository<Intent, Long> {
    Intent findByName(String name);
}
