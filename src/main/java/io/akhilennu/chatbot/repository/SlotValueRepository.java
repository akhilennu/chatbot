package io.akhilennu.chatbot.repository;

import io.akhilennu.chatbot.domain.SlotValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SlotValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlotValueRepository extends JpaRepository<SlotValue, Long> {}
