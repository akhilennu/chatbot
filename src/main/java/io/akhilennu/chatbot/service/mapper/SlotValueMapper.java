package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.Conversation;
import io.akhilennu.chatbot.domain.SlotValue;
import io.akhilennu.chatbot.service.dto.ConversationDTO;
import io.akhilennu.chatbot.service.dto.SlotValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SlotValue} and its DTO {@link SlotValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface SlotValueMapper extends EntityMapper<SlotValueDTO, SlotValue> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    SlotValueDTO toDto(SlotValue s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);
}
