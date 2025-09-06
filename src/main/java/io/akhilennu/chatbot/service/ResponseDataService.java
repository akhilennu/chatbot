package io.akhilennu.chatbot.service;

import io.akhilennu.chatbot.domain.ResponseData;
import io.akhilennu.chatbot.repository.ResponseDataRepository;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.mapper.ResponseDataMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.akhilennu.chatbot.domain.ResponseData}.
 */
@Service
@Transactional
public class ResponseDataService {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseDataService.class);

    private final ResponseDataRepository responseDataRepository;

    private final ResponseDataMapper responseDataMapper;

    public ResponseDataService(ResponseDataRepository responseDataRepository, ResponseDataMapper responseDataMapper) {
        this.responseDataRepository = responseDataRepository;
        this.responseDataMapper = responseDataMapper;
    }

    /**
     * Save a responseData.
     *
     * @param responseDataDTO the entity to save.
     * @return the persisted entity.
     */
    public ResponseDataDTO save(ResponseDataDTO responseDataDTO) {
        LOG.debug("Request to save ResponseData : {}", responseDataDTO);
        ResponseData responseData = responseDataMapper.toEntity(responseDataDTO);
        responseData = responseDataRepository.save(responseData);
        return responseDataMapper.toDto(responseData);
    }

    /**
     * Update a responseData.
     *
     * @param responseDataDTO the entity to save.
     * @return the persisted entity.
     */
    public ResponseDataDTO update(ResponseDataDTO responseDataDTO) {
        LOG.debug("Request to update ResponseData : {}", responseDataDTO);
        ResponseData responseData = responseDataMapper.toEntity(responseDataDTO);
        responseData = responseDataRepository.save(responseData);
        return responseDataMapper.toDto(responseData);
    }

    /**
     * Partially update a responseData.
     *
     * @param responseDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResponseDataDTO> partialUpdate(ResponseDataDTO responseDataDTO) {
        LOG.debug("Request to partially update ResponseData : {}", responseDataDTO);

        return responseDataRepository
            .findById(responseDataDTO.getId())
            .map(existingResponseData -> {
                responseDataMapper.partialUpdate(existingResponseData, responseDataDTO);

                return existingResponseData;
            })
            .map(responseDataRepository::save)
            .map(responseDataMapper::toDto);
    }

    /**
     * Get all the responseData.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResponseDataDTO> findAll() {
        LOG.debug("Request to get all ResponseData");
        return responseDataRepository.findAll().stream().map(responseDataMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the responseData where Intent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResponseDataDTO> findAllWhereIntentIsNull() {
        LOG.debug("Request to get all responseData where Intent is null");
        return StreamSupport.stream(responseDataRepository.findAll().spliterator(), false)
            .filter(responseData -> responseData.getIntent() == null)
            .map(responseDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the responseData where IntentEntity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResponseDataDTO> findAllWhereIntentEntityIsNull() {
        LOG.debug("Request to get all responseData where IntentEntity is null");
        return StreamSupport.stream(responseDataRepository.findAll().spliterator(), false)
            .filter(responseData -> responseData.getIntentEntity() == null)
            .map(responseDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one responseData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResponseDataDTO> findOne(Long id) {
        LOG.debug("Request to get ResponseData : {}", id);
        return responseDataRepository.findById(id).map(responseDataMapper::toDto);
    }

    /**
     * Delete the responseData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ResponseData : {}", id);
        responseDataRepository.deleteById(id);
    }
}
