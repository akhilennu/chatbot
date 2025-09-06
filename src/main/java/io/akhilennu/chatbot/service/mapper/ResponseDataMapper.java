package io.akhilennu.chatbot.service.mapper;

import io.akhilennu.chatbot.domain.ResponseData;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResponseData} and its DTO {@link ResponseDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResponseDataMapper extends EntityMapper<ResponseDataDTO, ResponseData> {}
