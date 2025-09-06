package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.ChatMessage;
import io.akhilennu.chatbot.domain.Conversation;
import io.akhilennu.chatbot.service.dto.ChatMessageDTO;
import io.akhilennu.chatbot.service.dto.ConversationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatMessage} and its DTO {@link ChatMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    ChatMessageDTO toDto(ChatMessage s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);
}
