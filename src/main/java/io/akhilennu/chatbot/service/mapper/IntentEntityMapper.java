package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.domain.IntentEntity;
import io.akhilennu.chatbot.domain.ResponseData;
import io.akhilennu.chatbot.service.dto.IntentDTO;
import io.akhilennu.chatbot.service.dto.IntentEntityDTO;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntentEntity} and its DTO {@link IntentEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntentEntityMapper extends EntityMapper<IntentEntityDTO, IntentEntity> {
    @Mapping(target = "missingEntityResponse", source = "missingEntityResponse", qualifiedByName = "responseDataId")
    @Mapping(target = "intent", source = "intent", qualifiedByName = "intentId")
    IntentEntityDTO toDto(IntentEntity s);

    @Named("responseDataId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResponseDataDTO toDtoResponseDataId(ResponseData responseData);

    @Named("intentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IntentDTO toDtoIntentId(Intent intent);
}
