package io.akhilennu.chatbot.service;

import io.akhilennu.chatbot.domain.SlotValue;
import io.akhilennu.chatbot.repository.SlotValueRepository;
import io.akhilennu.chatbot.service.dto.SlotValueDTO;
import io.akhilennu.chatbot.service.mapper.SlotValueMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.akhilennu.chatbot.domain.SlotValue}.
 */
@Service
@Transactional
public class SlotValueService {

    private static final Logger LOG = LoggerFactory.getLogger(SlotValueService.class);

    private final SlotValueRepository slotValueRepository;

    private final SlotValueMapper slotValueMapper;

    public SlotValueService(SlotValueRepository slotValueRepository, SlotValueMapper slotValueMapper) {
        this.slotValueRepository = slotValueRepository;
        this.slotValueMapper = slotValueMapper;
    }

    /**
     * Save a slotValue.
     *
     * @param slotValueDTO the entity to save.
     * @return the persisted entity.
     */
    public SlotValueDTO save(SlotValueDTO slotValueDTO) {
        LOG.debug("Request to save SlotValue : {}", slotValueDTO);
        SlotValue slotValue = slotValueMapper.toEntity(slotValueDTO);
        slotValue = slotValueRepository.save(slotValue);
        return slotValueMapper.toDto(slotValue);
    }

    /**
     * Update a slotValue.
     *
     * @param slotValueDTO the entity to save.
     * @return the persisted entity.
     */
    public SlotValueDTO update(SlotValueDTO slotValueDTO) {
        LOG.debug("Request to update SlotValue : {}", slotValueDTO);
        SlotValue slotValue = slotValueMapper.toEntity(slotValueDTO);
        slotValue = slotValueRepository.save(slotValue);
        return slotValueMapper.toDto(slotValue);
    }

    /**
     * Partially update a slotValue.
     *
     * @param slotValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SlotValueDTO> partialUpdate(SlotValueDTO slotValueDTO) {
        LOG.debug("Request to partially update SlotValue : {}", slotValueDTO);

        return slotValueRepository
            .findById(slotValueDTO.getId())
            .map(existingSlotValue -> {
                slotValueMapper.partialUpdate(existingSlotValue, slotValueDTO);

                return existingSlotValue;
            })
            .map(slotValueRepository::save)
            .map(slotValueMapper::toDto);
    }

    /**
     * Get all the slotValues.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SlotValueDTO> findAll() {
        LOG.debug("Request to get all SlotValues");
        return slotValueRepository.findAll().stream().map(slotValueMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one slotValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SlotValueDTO> findOne(Long id) {
        LOG.debug("Request to get SlotValue : {}", id);
        return slotValueRepository.findById(id).map(slotValueMapper::toDto);
    }

    /**
     * Delete the slotValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SlotValue : {}", id);
        slotValueRepository.deleteById(id);
    }
}
