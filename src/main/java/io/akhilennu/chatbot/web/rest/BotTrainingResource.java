package io.akhilennu.chatbot.web.rest;

import io.akhilennu.chatbot.repository.BotTrainingRepository;
import io.akhilennu.chatbot.service.BotTrainingService;
import io.akhilennu.chatbot.service.dto.BotTrainingDTO;
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
 * REST controller for managing {@link io.akhilennu.chatbot.domain.BotTraining}.
 */
@RestController
@RequestMapping("/api/bot-trainings")
public class BotTrainingResource {

    private static final Logger LOG = LoggerFactory.getLogger(BotTrainingResource.class);

    private static final String ENTITY_NAME = "botTraining";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BotTrainingService botTrainingService;

    private final BotTrainingRepository botTrainingRepository;

    public BotTrainingResource(BotTrainingService botTrainingService, BotTrainingRepository botTrainingRepository) {
        this.botTrainingService = botTrainingService;
        this.botTrainingRepository = botTrainingRepository;
    }

    /**
     * {@code POST  /bot-trainings} : Create a new botTraining.
     *
     * @param botTrainingDTO the botTrainingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new botTrainingDTO, or with status {@code 400 (Bad Request)} if the botTraining has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BotTrainingDTO> createBotTraining(@Valid @RequestBody BotTrainingDTO botTrainingDTO) throws URISyntaxException {
        LOG.debug("REST request to save BotTraining : {}", botTrainingDTO);
        if (botTrainingDTO.getId() != null) {
            throw new BadRequestAlertException("A new botTraining cannot already have an ID", ENTITY_NAME, "idexists");
        }
        botTrainingDTO = botTrainingService.save(botTrainingDTO);
        return ResponseEntity.created(new URI("/api/bot-trainings/" + botTrainingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, botTrainingDTO.getId().toString()))
            .body(botTrainingDTO);
    }

    /**
     * {@code PUT  /bot-trainings/:id} : Updates an existing botTraining.
     *
     * @param id the id of the botTrainingDTO to save.
     * @param botTrainingDTO the botTrainingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated botTrainingDTO,
     * or with status {@code 400 (Bad Request)} if the botTrainingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the botTrainingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BotTrainingDTO> updateBotTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BotTrainingDTO botTrainingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BotTraining : {}, {}", id, botTrainingDTO);
        if (botTrainingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, botTrainingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!botTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        botTrainingDTO = botTrainingService.update(botTrainingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, botTrainingDTO.getId().toString()))
            .body(botTrainingDTO);
    }

    /**
     * {@code PATCH  /bot-trainings/:id} : Partial updates given fields of an existing botTraining, field will ignore if it is null
     *
     * @param id the id of the botTrainingDTO to save.
     * @param botTrainingDTO the botTrainingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated botTrainingDTO,
     * or with status {@code 400 (Bad Request)} if the botTrainingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the botTrainingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the botTrainingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BotTrainingDTO> partialUpdateBotTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BotTrainingDTO botTrainingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BotTraining partially : {}, {}", id, botTrainingDTO);
        if (botTrainingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, botTrainingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!botTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BotTrainingDTO> result = botTrainingService.partialUpdate(botTrainingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, botTrainingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bot-trainings} : get all the botTrainings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of botTrainings in body.
     */
    @GetMapping("")
    public List<BotTrainingDTO> getAllBotTrainings() {
        LOG.debug("REST request to get all BotTrainings");
        return botTrainingService.findAll();
    }

    /**
     * {@code GET  /bot-trainings/:id} : get the "id" botTraining.
     *
     * @param id the id of the botTrainingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the botTrainingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BotTrainingDTO> getBotTraining(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BotTraining : {}", id);
        Optional<BotTrainingDTO> botTrainingDTO = botTrainingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(botTrainingDTO);
    }

    /**
     * {@code DELETE  /bot-trainings/:id} : delete the "id" botTraining.
     *
     * @param id the id of the botTrainingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBotTraining(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BotTraining : {}", id);
        botTrainingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
