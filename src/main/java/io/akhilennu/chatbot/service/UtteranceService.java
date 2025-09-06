package io.akhilennu.chatbot.service;

import io.akhilennu.chatbot.domain.Utterance;
import io.akhilennu.chatbot.repository.UtteranceRepository;
import io.akhilennu.chatbot.service.dto.UtteranceDTO;
import io.akhilennu.chatbot.service.mapper.UtteranceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.akhilennu.chatbot.domain.Utterance}.
 */
@Service
@Transactional
public class UtteranceService {

    private static final Logger LOG = LoggerFactory.getLogger(UtteranceService.class);

    private final UtteranceRepository utteranceRepository;

    private final UtteranceMapper utteranceMapper;

    public UtteranceService(UtteranceRepository utteranceRepository, UtteranceMapper utteranceMapper) {
        this.utteranceRepository = utteranceRepository;
        this.utteranceMapper = utteranceMapper;
    }

    /**
     * Save a utterance.
     *
     * @param utteranceDTO the entity to save.
     * @return the persisted entity.
     */
    public UtteranceDTO save(UtteranceDTO utteranceDTO) {
        LOG.debug("Request to save Utterance : {}", utteranceDTO);
        Utterance utterance = utteranceMapper.toEntity(utteranceDTO);
        utterance = utteranceRepository.save(utterance);
        return utteranceMapper.toDto(utterance);
    }

    /**
     * Update a utterance.
     *
     * @param utteranceDTO the entity to save.
     * @return the persisted entity.
     */
    public UtteranceDTO update(UtteranceDTO utteranceDTO) {
        LOG.debug("Request to update Utterance : {}", utteranceDTO);
        Utterance utterance = utteranceMapper.toEntity(utteranceDTO);
        utterance = utteranceRepository.save(utterance);
        return utteranceMapper.toDto(utterance);
    }

    /**
     * Partially update a utterance.
     *
     * @param utteranceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtteranceDTO> partialUpdate(UtteranceDTO utteranceDTO) {
        LOG.debug("Request to partially update Utterance : {}", utteranceDTO);

        return utteranceRepository
            .findById(utteranceDTO.getId())
            .map(existingUtterance -> {
                utteranceMapper.partialUpdate(existingUtterance, utteranceDTO);

                return existingUtterance;
            })
            .map(utteranceRepository::save)
            .map(utteranceMapper::toDto);
    }

    /**
     * Get all the utterances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UtteranceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Utterances");
        return utteranceRepository.findAll(pageable).map(utteranceMapper::toDto);
    }

    /**
     * Get one utterance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtteranceDTO> findOne(Long id) {
        LOG.debug("Request to get Utterance : {}", id);
        return utteranceRepository.findById(id).map(utteranceMapper::toDto);
    }

    /**
     * Delete the utterance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Utterance : {}", id);
        utteranceRepository.deleteById(id);
    }
}
