package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.domain.Utterance;
import io.akhilennu.chatbot.service.dto.IntentDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utterance} and its DTO {@link UtteranceDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtteranceMapper extends EntityMapper<UtteranceDTO, Utterance> {
    @Mapping(target = "intent", source = "intent", qualifiedByName = "intentId")
    UtteranceDTO toDto(Utterance s);

    @Named("intentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IntentDTO toDtoIntentId(Intent intent);
}
