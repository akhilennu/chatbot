package io.akhilennu.chatbot.service;

import io.akhilennu.chatbot.domain.UtteranceIntent;
import io.akhilennu.chatbot.repository.UtteranceIntentRepository;
import io.akhilennu.chatbot.service.dto.UtteranceIntentDTO;
import io.akhilennu.chatbot.service.mapper.UtteranceIntentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.akhilennu.chatbot.domain.UtteranceIntent}.
 */
@Service
@Transactional
public class UtteranceIntentService {

    private static final Logger LOG = LoggerFactory.getLogger(UtteranceIntentService.class);

    private final UtteranceIntentRepository utteranceIntentRepository;

    private final UtteranceIntentMapper utteranceIntentMapper;

    public UtteranceIntentService(UtteranceIntentRepository utteranceIntentRepository, UtteranceIntentMapper utteranceIntentMapper) {
        this.utteranceIntentRepository = utteranceIntentRepository;
        this.utteranceIntentMapper = utteranceIntentMapper;
    }

    /**
     * Save a utteranceIntent.
     *
     * @param utteranceIntentDTO the entity to save.
     * @return the persisted entity.
     */
    public UtteranceIntentDTO save(UtteranceIntentDTO utteranceIntentDTO) {
        LOG.debug("Request to save UtteranceIntent : {}", utteranceIntentDTO);
        UtteranceIntent utteranceIntent = utteranceIntentMapper.toEntity(utteranceIntentDTO);
        utteranceIntent = utteranceIntentRepository.save(utteranceIntent);
        return utteranceIntentMapper.toDto(utteranceIntent);
    }

    /**
     * Update a utteranceIntent.
     *
     * @param utteranceIntentDTO the entity to save.
     * @return the persisted entity.
     */
    public UtteranceIntentDTO update(UtteranceIntentDTO utteranceIntentDTO) {
        LOG.debug("Request to update UtteranceIntent : {}", utteranceIntentDTO);
        UtteranceIntent utteranceIntent = utteranceIntentMapper.toEntity(utteranceIntentDTO);
        utteranceIntent = utteranceIntentRepository.save(utteranceIntent);
        return utteranceIntentMapper.toDto(utteranceIntent);
    }

    /**
     * Partially update a utteranceIntent.
     *
     * @param utteranceIntentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtteranceIntentDTO> partialUpdate(UtteranceIntentDTO utteranceIntentDTO) {
        LOG.debug("Request to partially update UtteranceIntent : {}", utteranceIntentDTO);

        return utteranceIntentRepository
            .findById(utteranceIntentDTO.getId())
            .map(existingUtteranceIntent -> {
                utteranceIntentMapper.partialUpdate(existingUtteranceIntent, utteranceIntentDTO);

                return existingUtteranceIntent;
            })
            .map(utteranceIntentRepository::save)
            .map(utteranceIntentMapper::toDto);
    }

    /**
     * Get all the utteranceIntents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtteranceIntentDTO> findAll() {
        LOG.debug("Request to get all UtteranceIntents");
        return utteranceIntentRepository
            .findAll()
            .stream()
            .map(utteranceIntentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one utteranceIntent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtteranceIntentDTO> findOne(Long id) {
        LOG.debug("Request to get UtteranceIntent : {}", id);
        return utteranceIntentRepository.findById(id).map(utteranceIntentMapper::toDto);
    }

    /**
     * Delete the utteranceIntent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UtteranceIntent : {}", id);
        utteranceIntentRepository.deleteById(id);
    }
}
