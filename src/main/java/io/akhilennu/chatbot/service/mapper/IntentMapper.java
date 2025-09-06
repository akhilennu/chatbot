package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.Bot;
import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.domain.ResponseData;
import io.akhilennu.chatbot.service.dto.BotDTO;
import io.akhilennu.chatbot.service.dto.IntentDTO;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Intent} and its DTO {@link IntentDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntentMapper extends EntityMapper<IntentDTO, Intent> {
    @Mapping(target = "responseData", source = "responseData", qualifiedByName = "responseDataId")
    @Mapping(target = "bot", source = "bot", qualifiedByName = "botId")
    IntentDTO toDto(Intent s);

    @Named("responseDataId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResponseDataDTO toDtoResponseDataId(ResponseData responseData);

    @Named("botId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BotDTO toDtoBotId(Bot bot);
}
