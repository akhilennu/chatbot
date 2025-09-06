package io.akhilennu.chatbot.repository;

import io.akhilennu.chatbot.domain.ResponseData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResponseData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponseDataRepository extends JpaRepository<ResponseData, Long> {}
