package io.akhilennu.chatbot.web.rest;

import io.akhilennu.chatbot.repository.UtteranceIntentRepository;
import io.akhilennu.chatbot.service.UtteranceIntentService;
import io.akhilennu.chatbot.service.dto.UtteranceIntentDTO;
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
 * REST controller for managing {@link io.akhilennu.chatbot.domain.UtteranceIntent}.
 */
@RestController
@RequestMapping("/api/utterance-intents")
public class UtteranceIntentResource {

    private static final Logger LOG = LoggerFactory.getLogger(UtteranceIntentResource.class);

    private static final String ENTITY_NAME = "utteranceIntent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtteranceIntentService utteranceIntentService;

    private final UtteranceIntentRepository utteranceIntentRepository;

    public UtteranceIntentResource(UtteranceIntentService utteranceIntentService, UtteranceIntentRepository utteranceIntentRepository) {
        this.utteranceIntentService = utteranceIntentService;
        this.utteranceIntentRepository = utteranceIntentRepository;
    }

    /**
     * {@code POST  /utterance-intents} : Create a new utteranceIntent.
     *
     * @param utteranceIntentDTO the utteranceIntentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utteranceIntentDTO, or with status {@code 400 (Bad Request)} if the utteranceIntent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UtteranceIntentDTO> createUtteranceIntent(@Valid @RequestBody UtteranceIntentDTO utteranceIntentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UtteranceIntent : {}", utteranceIntentDTO);
        if (utteranceIntentDTO.getId() != null) {
            throw new BadRequestAlertException("A new utteranceIntent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        utteranceIntentDTO = utteranceIntentService.save(utteranceIntentDTO);
        return ResponseEntity.created(new URI("/api/utterance-intents/" + utteranceIntentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, utteranceIntentDTO.getId().toString()))
            .body(utteranceIntentDTO);
    }

    /**
     * {@code PUT  /utterance-intents/:id} : Updates an existing utteranceIntent.
     *
     * @param id the id of the utteranceIntentDTO to save.
     * @param utteranceIntentDTO the utteranceIntentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utteranceIntentDTO,
     * or with status {@code 400 (Bad Request)} if the utteranceIntentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utteranceIntentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtteranceIntentDTO> updateUtteranceIntent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UtteranceIntentDTO utteranceIntentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UtteranceIntent : {}, {}", id, utteranceIntentDTO);
        if (utteranceIntentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utteranceIntentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utteranceIntentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        utteranceIntentDTO = utteranceIntentService.update(utteranceIntentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, utteranceIntentDTO.getId().toString()))
            .body(utteranceIntentDTO);
    }

    /**
     * {@code PATCH  /utterance-intents/:id} : Partial updates given fields of an existing utteranceIntent, field will ignore if it is null
     *
     * @param id the id of the utteranceIntentDTO to save.
     * @param utteranceIntentDTO the utteranceIntentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utteranceIntentDTO,
     * or with status {@code 400 (Bad Request)} if the utteranceIntentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the utteranceIntentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the utteranceIntentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtteranceIntentDTO> partialUpdateUtteranceIntent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UtteranceIntentDTO utteranceIntentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UtteranceIntent partially : {}, {}", id, utteranceIntentDTO);
        if (utteranceIntentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utteranceIntentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utteranceIntentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtteranceIntentDTO> result = utteranceIntentService.partialUpdate(utteranceIntentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, utteranceIntentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /utterance-intents} : get all the utteranceIntents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utteranceIntents in body.
     */
    @GetMapping("")
    public List<UtteranceIntentDTO> getAllUtteranceIntents() {
        LOG.debug("REST request to get all UtteranceIntents");
        return utteranceIntentService.findAll();
    }

    /**
     * {@code GET  /utterance-intents/:id} : get the "id" utteranceIntent.
     *
     * @param id the id of the utteranceIntentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utteranceIntentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtteranceIntentDTO> getUtteranceIntent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UtteranceIntent : {}", id);
        Optional<UtteranceIntentDTO> utteranceIntentDTO = utteranceIntentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utteranceIntentDTO);
    }

    /**
     * {@code DELETE  /utterance-intents/:id} : delete the "id" utteranceIntent.
     *
     * @param id the id of the utteranceIntentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtteranceIntent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UtteranceIntent : {}", id);
        utteranceIntentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
