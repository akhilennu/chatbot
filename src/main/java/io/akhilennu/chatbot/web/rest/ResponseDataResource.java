package io.akhilennu.chatbot.web.rest;

import io.akhilennu.chatbot.repository.ResponseDataRepository;
import io.akhilennu.chatbot.service.ResponseDataService;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link io.akhilennu.chatbot.domain.ResponseData}.
 */
@RestController
@RequestMapping("/api/response-data")
public class ResponseDataResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseDataResource.class);

    private static final String ENTITY_NAME = "responseData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponseDataService responseDataService;

    private final ResponseDataRepository responseDataRepository;

    public ResponseDataResource(ResponseDataService responseDataService, ResponseDataRepository responseDataRepository) {
        this.responseDataService = responseDataService;
        this.responseDataRepository = responseDataRepository;
    }

    /**
     * {@code POST  /response-data} : Create a new responseData.
     *
     * @param responseDataDTO the responseDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responseDataDTO, or with status {@code 400 (Bad Request)} if the responseData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ResponseDataDTO> createResponseData(@RequestBody ResponseDataDTO responseDataDTO) throws URISyntaxException {
        LOG.debug("REST request to save ResponseData : {}", responseDataDTO);
        if (responseDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new responseData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        responseDataDTO = responseDataService.save(responseDataDTO);
        return ResponseEntity.created(new URI("/api/response-data/" + responseDataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, responseDataDTO.getId().toString()))
            .body(responseDataDTO);
    }

    /**
     * {@code PUT  /response-data/:id} : Updates an existing responseData.
     *
     * @param id the id of the responseDataDTO to save.
     * @param responseDataDTO the responseDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responseDataDTO,
     * or with status {@code 400 (Bad Request)} if the responseDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responseDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDataDTO> updateResponseData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResponseDataDTO responseDataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ResponseData : {}, {}", id, responseDataDTO);
        if (responseDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responseDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responseDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        responseDataDTO = responseDataService.update(responseDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, responseDataDTO.getId().toString()))
            .body(responseDataDTO);
    }

    /**
     * {@code PATCH  /response-data/:id} : Partial updates given fields of an existing responseData, field will ignore if it is null
     *
     * @param id the id of the responseDataDTO to save.
     * @param responseDataDTO the responseDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responseDataDTO,
     * or with status {@code 400 (Bad Request)} if the responseDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the responseDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the responseDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResponseDataDTO> partialUpdateResponseData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResponseDataDTO responseDataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ResponseData partially : {}, {}", id, responseDataDTO);
        if (responseDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responseDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responseDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResponseDataDTO> result = responseDataService.partialUpdate(responseDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, responseDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /response-data} : get all the responseData.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responseData in body.
     */
    @GetMapping("")
    public List<ResponseDataDTO> getAllResponseData(@RequestParam(name = "filter", required = false) String filter) {
        if ("intent-is-null".equals(filter)) {
            LOG.debug("REST request to get all ResponseDatas where intent is null");
            return responseDataService.findAllWhereIntentIsNull();
        }

        if ("intententity-is-null".equals(filter)) {
            LOG.debug("REST request to get all ResponseDatas where intentEntity is null");
            return responseDataService.findAllWhereIntentEntityIsNull();
        }
        LOG.debug("REST request to get all ResponseData");
        return responseDataService.findAll();
    }

    /**
     * {@code GET  /response-data/:id} : get the "id" responseData.
     *
     * @param id the id of the responseDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responseDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataDTO> getResponseData(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ResponseData : {}", id);
        Optional<ResponseDataDTO> responseDataDTO = responseDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(responseDataDTO);
    }

    /**
     * {@code DELETE  /response-data/:id} : delete the "id" responseData.
     *
     * @param id the id of the responseDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponseData(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ResponseData : {}", id);
        responseDataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
