package io.akhilennu.chatbot.web.rest;

import static io.akhilennu.chatbot.domain.UtteranceIntentAsserts.*;
import static io.akhilennu.chatbot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.akhilennu.chatbot.IntegrationTest;
import io.akhilennu.chatbot.domain.UtteranceIntent;
import io.akhilennu.chatbot.repository.UtteranceIntentRepository;
import io.akhilennu.chatbot.service.dto.UtteranceIntentDTO;
import io.akhilennu.chatbot.service.mapper.UtteranceIntentMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtteranceIntentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtteranceIntentResourceIT {

    private static final Integer DEFAULT_START_INDEX = 1;
    private static final Integer UPDATED_START_INDEX = 2;

    private static final Integer DEFAULT_END_INDEX = 1;
    private static final Integer UPDATED_END_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/utterance-intents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtteranceIntentRepository utteranceIntentRepository;

    @Autowired
    private UtteranceIntentMapper utteranceIntentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtteranceIntentMockMvc;

    private UtteranceIntent utteranceIntent;

    private UtteranceIntent insertedUtteranceIntent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtteranceIntent createEntity() {
        return new UtteranceIntent().startIndex(DEFAULT_START_INDEX).endIndex(DEFAULT_END_INDEX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtteranceIntent createUpdatedEntity() {
        return new UtteranceIntent().startIndex(UPDATED_START_INDEX).endIndex(UPDATED_END_INDEX);
    }

    @BeforeEach
    void initTest() {
        utteranceIntent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUtteranceIntent != null) {
            utteranceIntentRepository.delete(insertedUtteranceIntent);
            insertedUtteranceIntent = null;
        }
    }

    @Test
    @Transactional
    void createUtteranceIntent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);
        var returnedUtteranceIntentDTO = om.readValue(
            restUtteranceIntentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utteranceIntentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UtteranceIntentDTO.class
        );

        // Validate the UtteranceIntent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUtteranceIntent = utteranceIntentMapper.toEntity(returnedUtteranceIntentDTO);
        assertUtteranceIntentUpdatableFieldsEquals(returnedUtteranceIntent, getPersistedUtteranceIntent(returnedUtteranceIntent));

        insertedUtteranceIntent = returnedUtteranceIntent;
    }

    @Test
    @Transactional
    void createUtteranceIntentWithExistingId() throws Exception {
        // Create the UtteranceIntent with an existing ID
        utteranceIntent.setId(1L);
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtteranceIntentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utteranceIntentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utteranceIntent.setStartIndex(null);

        // Create the UtteranceIntent, which fails.
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        restUtteranceIntentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utteranceIntentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utteranceIntent.setEndIndex(null);

        // Create the UtteranceIntent, which fails.
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        restUtteranceIntentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utteranceIntentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtteranceIntents() throws Exception {
        // Initialize the database
        insertedUtteranceIntent = utteranceIntentRepository.saveAndFlush(utteranceIntent);

        // Get all the utteranceIntentList
        restUtteranceIntentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utteranceIntent.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)));
    }

    @Test
    @Transactional
    void getUtteranceIntent() throws Exception {
        // Initialize the database
        insertedUtteranceIntent = utteranceIntentRepository.saveAndFlush(utteranceIntent);

        // Get the utteranceIntent
        restUtteranceIntentMockMvc
            .perform(get(ENTITY_API_URL_ID, utteranceIntent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utteranceIntent.getId().intValue()))
            .andExpect(jsonPath("$.startIndex").value(DEFAULT_START_INDEX))
            .andExpect(jsonPath("$.endIndex").value(DEFAULT_END_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingUtteranceIntent() throws Exception {
        // Get the utteranceIntent
        restUtteranceIntentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtteranceIntent() throws Exception {
        // Initialize the database
        insertedUtteranceIntent = utteranceIntentRepository.saveAndFlush(utteranceIntent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utteranceIntent
        UtteranceIntent updatedUtteranceIntent = utteranceIntentRepository.findById(utteranceIntent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtteranceIntent are not directly saved in db
        em.detach(updatedUtteranceIntent);
        updatedUtteranceIntent.startIndex(UPDATED_START_INDEX).endIndex(UPDATED_END_INDEX);
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(updatedUtteranceIntent);

        restUtteranceIntentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utteranceIntentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utteranceIntentDTO))
            )
            .andExpect(status().isOk());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtteranceIntentToMatchAllProperties(updatedUtteranceIntent);
    }

    @Test
    @Transactional
    void putNonExistingUtteranceIntent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utteranceIntent.setId(longCount.incrementAndGet());

        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtteranceIntentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utteranceIntentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utteranceIntentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtteranceIntent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utteranceIntent.setId(longCount.incrementAndGet());

        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceIntentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utteranceIntentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtteranceIntent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utteranceIntent.setId(longCount.incrementAndGet());

        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceIntentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utteranceIntentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtteranceIntentWithPatch() throws Exception {
        // Initialize the database
        insertedUtteranceIntent = utteranceIntentRepository.saveAndFlush(utteranceIntent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utteranceIntent using partial update
        UtteranceIntent partialUpdatedUtteranceIntent = new UtteranceIntent();
        partialUpdatedUtteranceIntent.setId(utteranceIntent.getId());

        partialUpdatedUtteranceIntent.startIndex(UPDATED_START_INDEX);

        restUtteranceIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtteranceIntent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtteranceIntent))
            )
            .andExpect(status().isOk());

        // Validate the UtteranceIntent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtteranceIntentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtteranceIntent, utteranceIntent),
            getPersistedUtteranceIntent(utteranceIntent)
        );
    }

    @Test
    @Transactional
    void fullUpdateUtteranceIntentWithPatch() throws Exception {
        // Initialize the database
        insertedUtteranceIntent = utteranceIntentRepository.saveAndFlush(utteranceIntent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utteranceIntent using partial update
        UtteranceIntent partialUpdatedUtteranceIntent = new UtteranceIntent();
        partialUpdatedUtteranceIntent.setId(utteranceIntent.getId());

        partialUpdatedUtteranceIntent.startIndex(UPDATED_START_INDEX).endIndex(UPDATED_END_INDEX);

        restUtteranceIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtteranceIntent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtteranceIntent))
            )
            .andExpect(status().isOk());

        // Validate the UtteranceIntent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtteranceIntentUpdatableFieldsEquals(
            partialUpdatedUtteranceIntent,
            getPersistedUtteranceIntent(partialUpdatedUtteranceIntent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUtteranceIntent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utteranceIntent.setId(longCount.incrementAndGet());

        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtteranceIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utteranceIntentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utteranceIntentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtteranceIntent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utteranceIntent.setId(longCount.incrementAndGet());

        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utteranceIntentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtteranceIntent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utteranceIntent.setId(longCount.incrementAndGet());

        // Create the UtteranceIntent
        UtteranceIntentDTO utteranceIntentDTO = utteranceIntentMapper.toDto(utteranceIntent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceIntentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(utteranceIntentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtteranceIntent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtteranceIntent() throws Exception {
        // Initialize the database
        insertedUtteranceIntent = utteranceIntentRepository.saveAndFlush(utteranceIntent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utteranceIntent
        restUtteranceIntentMockMvc
            .perform(delete(ENTITY_API_URL_ID, utteranceIntent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utteranceIntentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UtteranceIntent getPersistedUtteranceIntent(UtteranceIntent utteranceIntent) {
        return utteranceIntentRepository.findById(utteranceIntent.getId()).orElseThrow();
    }

    protected void assertPersistedUtteranceIntentToMatchAllProperties(UtteranceIntent expectedUtteranceIntent) {
        assertUtteranceIntentAllPropertiesEquals(expectedUtteranceIntent, getPersistedUtteranceIntent(expectedUtteranceIntent));
    }

    protected void assertPersistedUtteranceIntentToMatchUpdatableProperties(UtteranceIntent expectedUtteranceIntent) {
        assertUtteranceIntentAllUpdatablePropertiesEquals(expectedUtteranceIntent, getPersistedUtteranceIntent(expectedUtteranceIntent));
    }
}
