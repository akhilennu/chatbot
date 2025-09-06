package io.akhilennu.chatbot.web.rest;

import io.akhilennu.chatbot.repository.SlotValueRepository;
import io.akhilennu.chatbot.service.SlotValueService;
import io.akhilennu.chatbot.service.dto.SlotValueDTO;
import io.akhilennu.chatbot.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.akhilennu.chatbot.domain.SlotValue}.
 */
@RestController
@RequestMapping("/api/slot-values")
public class SlotValueResource {

    private static final Logger LOG = LoggerFactory.getLogger(SlotValueResource.class);

    private static final String ENTITY_NAME = "slotValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlotValueService slotValueService;

    private final SlotValueRepository slotValueRepository;

    public SlotValueResource(SlotValueService slotValueService, SlotValueRepository slotValueRepository) {
        this.slotValueService = slotValueService;
        this.slotValueRepository = slotValueRepository;
    }

    /**
     * {@code POST  /slot-values} : Create a new slotValue.
     *
     * @param slotValueDTO the slotValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slotValueDTO, or with status {@code 400 (Bad Request)} if the slotValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SlotValueDTO> createSlotValue(@Valid @RequestBody SlotValueDTO slotValueDTO) throws URISyntaxException {
        LOG.debug("REST request to save SlotValue : {}", slotValueDTO);
        if (slotValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new slotValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        slotValueDTO = slotValueService.save(slotValueDTO);
        return ResponseEntity.created(new URI("/api/slot-values/" + slotValueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, slotValueDTO.getId().toString()))
            .body(slotValueDTO);
    }

    /**
     * {@code PUT  /slot-values/:id} : Updates an existing slotValue.
     *
     * @param id the id of the slotValueDTO to save.
     * @param slotValueDTO the slotValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotValueDTO,
     * or with status {@code 400 (Bad Request)} if the slotValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slotValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SlotValueDTO> updateSlotValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SlotValueDTO slotValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SlotValue : {}, {}", id, slotValueDTO);
        if (slotValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slotValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slotValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        slotValueDTO = slotValueService.update(slotValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotValueDTO.getId().toString()))
            .body(slotValueDTO);
    }

    /**
     * {@code PATCH  /slot-values/:id} : Partial updates given fields of an existing slotValue, field will ignore if it is null
     *
     * @param id the id of the slotValueDTO to save.
     * @param slotValueDTO the slotValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotValueDTO,
     * or with status {@code 400 (Bad Request)} if the slotValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the slotValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the slotValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SlotValueDTO> partialUpdateSlotValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SlotValueDTO slotValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SlotValue partially : {}, {}", id, slotValueDTO);
        if (slotValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slotValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slotValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SlotValueDTO> result = slotValueService.partialUpdate(slotValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /slot-values} : get all the slotValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slotValues in body.
     */
    @GetMapping("")
    public List<SlotValueDTO> getAllSlotValues() {
        LOG.debug("REST request to get all SlotValues");
        return slotValueService.findAll();
    }

    /**
     * {@code GET  /slot-values/:id} : get the "id" slotValue.
     *
     * @param id the id of the slotValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slotValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SlotValueDTO> getSlotValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SlotValue : {}", id);
        Optional<SlotValueDTO> slotValueDTO = slotValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slotValueDTO);
    }

    /**
     * {@code DELETE  /slot-values/:id} : delete the "id" slotValue.
     *
     * @param id the id of the slotValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlotValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SlotValue : {}", id);
        slotValueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
