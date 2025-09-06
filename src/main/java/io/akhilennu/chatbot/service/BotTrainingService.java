package io.akhilennu.chatbot.service;

import io.akhilennu.chatbot.domain.BotTraining;
import io.akhilennu.chatbot.repository.BotTrainingRepository;
import io.akhilennu.chatbot.service.dto.BotTrainingDTO;
import io.akhilennu.chatbot.service.mapper.BotTrainingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.akhilennu.chatbot.domain.BotTraining}.
 */
@Service
@Transactional
public class BotTrainingService {

    private static final Logger LOG = LoggerFactory.getLogger(BotTrainingService.class);

    private final BotTrainingRepository botTrainingRepository;

    private final BotTrainingMapper botTrainingMapper;

    public BotTrainingService(BotTrainingRepository botTrainingRepository, BotTrainingMapper botTrainingMapper) {
        this.botTrainingRepository = botTrainingRepository;
        this.botTrainingMapper = botTrainingMapper;
    }

    /**
     * Save a botTraining.
     *
     * @param botTrainingDTO the entity to save.
     * @return the persisted entity.
     */
    public BotTrainingDTO save(BotTrainingDTO botTrainingDTO) {
        LOG.debug("Request to save BotTraining : {}", botTrainingDTO);
        BotTraining botTraining = botTrainingMapper.toEntity(botTrainingDTO);
        botTraining = botTrainingRepository.save(botTraining);
        return botTrainingMapper.toDto(botTraining);
    }

    /**
     * Update a botTraining.
     *
     * @param botTrainingDTO the entity to save.
     * @return the persisted entity.
     */
    public BotTrainingDTO update(BotTrainingDTO botTrainingDTO) {
        LOG.debug("Request to update BotTraining : {}", botTrainingDTO);
        BotTraining botTraining = botTrainingMapper.toEntity(botTrainingDTO);
        botTraining = botTrainingRepository.save(botTraining);
        return botTrainingMapper.toDto(botTraining);
    }

    /**
     * Partially update a botTraining.
     *
     * @param botTrainingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BotTrainingDTO> partialUpdate(BotTrainingDTO botTrainingDTO) {
        LOG.debug("Request to partially update BotTraining : {}", botTrainingDTO);

        return botTrainingRepository
            .findById(botTrainingDTO.getId())
            .map(existingBotTraining -> {
                botTrainingMapper.partialUpdate(existingBotTraining, botTrainingDTO);

                return existingBotTraining;
            })
            .map(botTrainingRepository::save)
            .map(botTrainingMapper::toDto);
    }

    /**
     * Get all the botTrainings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BotTrainingDTO> findAll() {
        LOG.debug("Request to get all BotTrainings");
        return botTrainingRepository.findAll().stream().map(botTrainingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one botTraining by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BotTrainingDTO> findOne(Long id) {
        LOG.debug("Request to get BotTraining : {}", id);
        return botTrainingRepository.findById(id).map(botTrainingMapper::toDto);
    }

    /**
     * Delete the botTraining by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BotTraining : {}", id);
        botTrainingRepository.deleteById(id);
    }
}
