package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.Conversation;
import io.akhilennu.chatbot.service.dto.ConversationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {}
