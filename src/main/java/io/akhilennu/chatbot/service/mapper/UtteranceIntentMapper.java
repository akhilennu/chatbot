package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.IntentEntity;
import io.akhilennu.chatbot.domain.Utterance;
import io.akhilennu.chatbot.domain.UtteranceIntent;
import io.akhilennu.chatbot.service.dto.IntentEntityDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDTO;
import io.akhilennu.chatbot.service.dto.UtteranceIntentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UtteranceIntent} and its DTO {@link UtteranceIntentDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtteranceIntentMapper extends EntityMapper<UtteranceIntentDTO, UtteranceIntent> {
    @Mapping(target = "utterance", source = "utterance", qualifiedByName = "utteranceId")
    @Mapping(target = "intentEntity", source = "intentEntity", qualifiedByName = "intentEntityId")
    UtteranceIntentDTO toDto(UtteranceIntent s);

    @Named("utteranceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtteranceDTO toDtoUtteranceId(Utterance utterance);

    @Named("intentEntityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IntentEntityDTO toDtoIntentEntityId(IntentEntity intentEntity);
}
