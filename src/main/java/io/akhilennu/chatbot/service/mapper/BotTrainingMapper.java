package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.Bot;
import io.akhilennu.chatbot.domain.BotTraining;
import io.akhilennu.chatbot.service.dto.BotDTO;
import io.akhilennu.chatbot.service.dto.BotTrainingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BotTraining} and its DTO {@link BotTrainingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BotTrainingMapper extends EntityMapper<BotTrainingDTO, BotTraining> {
    @Mapping(target = "bot", source = "bot", qualifiedByName = "botId")
    BotTrainingDTO toDto(BotTraining s);

    @Named("botId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BotDTO toDtoBotId(Bot bot);
}
