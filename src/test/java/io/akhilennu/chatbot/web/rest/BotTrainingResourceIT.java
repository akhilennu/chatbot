package io.akhilennu.chatbot.web.rest;

import static io.akhilennu.chatbot.domain.BotTrainingAsserts.*;
import static io.akhilennu.chatbot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.akhilennu.chatbot.IntegrationTest;
import io.akhilennu.chatbot.domain.BotTraining;
import io.akhilennu.chatbot.repository.BotTrainingRepository;
import io.akhilennu.chatbot.service.dto.BotTrainingDTO;
import io.akhilennu.chatbot.service.mapper.BotTrainingMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link BotTrainingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BotTrainingResourceIT {

    private static final Instant DEFAULT_TRAINING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRAINING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_ACCURACY = 1D;
    private static final Double UPDATED_ACCURACY = 2D;

    private static final Double DEFAULT_PRECISION = 1D;
    private static final Double UPDATED_PRECISION = 2D;

    private static final Double DEFAULT_F_1_SCORE = 1D;
    private static final Double UPDATED_F_1_SCORE = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/bot-trainings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BotTrainingRepository botTrainingRepository;

    @Autowired
    private BotTrainingMapper botTrainingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBotTrainingMockMvc;

    private BotTraining botTraining;

    private BotTraining insertedBotTraining;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BotTraining createEntity() {
        return new BotTraining()
            .trainingTime(DEFAULT_TRAINING_TIME)
            .accuracy(DEFAULT_ACCURACY)
            .precision(DEFAULT_PRECISION)
            .f1Score(DEFAULT_F_1_SCORE)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BotTraining createUpdatedEntity() {
        return new BotTraining()
            .trainingTime(UPDATED_TRAINING_TIME)
            .accuracy(UPDATED_ACCURACY)
            .precision(UPDATED_PRECISION)
            .f1Score(UPDATED_F_1_SCORE)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        botTraining = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBotTraining != null) {
            botTrainingRepository.delete(insertedBotTraining);
            insertedBotTraining = null;
        }
    }

    @Test
    @Transactional
    void createBotTraining() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);
        var returnedBotTrainingDTO = om.readValue(
            restBotTrainingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(botTrainingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BotTrainingDTO.class
        );

        // Validate the BotTraining in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBotTraining = botTrainingMapper.toEntity(returnedBotTrainingDTO);
        assertBotTrainingUpdatableFieldsEquals(returnedBotTraining, getPersistedBotTraining(returnedBotTraining));

        insertedBotTraining = returnedBotTraining;
    }

    @Test
    @Transactional
    void createBotTrainingWithExistingId() throws Exception {
        // Create the BotTraining with an existing ID
        botTraining.setId(1L);
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBotTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(botTrainingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrainingTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        botTraining.setTrainingTime(null);

        // Create the BotTraining, which fails.
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        restBotTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(botTrainingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBotTrainings() throws Exception {
        // Initialize the database
        insertedBotTraining = botTrainingRepository.saveAndFlush(botTraining);

        // Get all the botTrainingList
        restBotTrainingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(botTraining.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingTime").value(hasItem(DEFAULT_TRAINING_TIME.toString())))
            .andExpect(jsonPath("$.[*].accuracy").value(hasItem(DEFAULT_ACCURACY)))
            .andExpect(jsonPath("$.[*].precision").value(hasItem(DEFAULT_PRECISION)))
            .andExpect(jsonPath("$.[*].f1Score").value(hasItem(DEFAULT_F_1_SCORE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getBotTraining() throws Exception {
        // Initialize the database
        insertedBotTraining = botTrainingRepository.saveAndFlush(botTraining);

        // Get the botTraining
        restBotTrainingMockMvc
            .perform(get(ENTITY_API_URL_ID, botTraining.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(botTraining.getId().intValue()))
            .andExpect(jsonPath("$.trainingTime").value(DEFAULT_TRAINING_TIME.toString()))
            .andExpect(jsonPath("$.accuracy").value(DEFAULT_ACCURACY))
            .andExpect(jsonPath("$.precision").value(DEFAULT_PRECISION))
            .andExpect(jsonPath("$.f1Score").value(DEFAULT_F_1_SCORE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingBotTraining() throws Exception {
        // Get the botTraining
        restBotTrainingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBotTraining() throws Exception {
        // Initialize the database
        insertedBotTraining = botTrainingRepository.saveAndFlush(botTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the botTraining
        BotTraining updatedBotTraining = botTrainingRepository.findById(botTraining.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBotTraining are not directly saved in db
        em.detach(updatedBotTraining);
        updatedBotTraining
            .trainingTime(UPDATED_TRAINING_TIME)
            .accuracy(UPDATED_ACCURACY)
            .precision(UPDATED_PRECISION)
            .f1Score(UPDATED_F_1_SCORE)
            .active(UPDATED_ACTIVE);
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(updatedBotTraining);

        restBotTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, botTrainingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(botTrainingDTO))
            )
            .andExpect(status().isOk());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBotTrainingToMatchAllProperties(updatedBotTraining);
    }

    @Test
    @Transactional
    void putNonExistingBotTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        botTraining.setId(longCount.incrementAndGet());

        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBotTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, botTrainingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(botTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBotTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        botTraining.setId(longCount.incrementAndGet());

        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBotTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(botTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBotTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        botTraining.setId(longCount.incrementAndGet());

        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBotTrainingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(botTrainingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBotTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedBotTraining = botTrainingRepository.saveAndFlush(botTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the botTraining using partial update
        BotTraining partialUpdatedBotTraining = new BotTraining();
        partialUpdatedBotTraining.setId(botTraining.getId());

        partialUpdatedBotTraining
            .trainingTime(UPDATED_TRAINING_TIME)
            .accuracy(UPDATED_ACCURACY)
            .precision(UPDATED_PRECISION)
            .f1Score(UPDATED_F_1_SCORE)
            .active(UPDATED_ACTIVE);

        restBotTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBotTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBotTraining))
            )
            .andExpect(status().isOk());

        // Validate the BotTraining in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBotTrainingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBotTraining, botTraining),
            getPersistedBotTraining(botTraining)
        );
    }

    @Test
    @Transactional
    void fullUpdateBotTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedBotTraining = botTrainingRepository.saveAndFlush(botTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the botTraining using partial update
        BotTraining partialUpdatedBotTraining = new BotTraining();
        partialUpdatedBotTraining.setId(botTraining.getId());

        partialUpdatedBotTraining
            .trainingTime(UPDATED_TRAINING_TIME)
            .accuracy(UPDATED_ACCURACY)
            .precision(UPDATED_PRECISION)
            .f1Score(UPDATED_F_1_SCORE)
            .active(UPDATED_ACTIVE);

        restBotTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBotTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBotTraining))
            )
            .andExpect(status().isOk());

        // Validate the BotTraining in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBotTrainingUpdatableFieldsEquals(partialUpdatedBotTraining, getPersistedBotTraining(partialUpdatedBotTraining));
    }

    @Test
    @Transactional
    void patchNonExistingBotTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        botTraining.setId(longCount.incrementAndGet());

        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBotTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, botTrainingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(botTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBotTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        botTraining.setId(longCount.incrementAndGet());

        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBotTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(botTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBotTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        botTraining.setId(longCount.incrementAndGet());

        // Create the BotTraining
        BotTrainingDTO botTrainingDTO = botTrainingMapper.toDto(botTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBotTrainingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(botTrainingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BotTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBotTraining() throws Exception {
        // Initialize the database
        insertedBotTraining = botTrainingRepository.saveAndFlush(botTraining);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the botTraining
        restBotTrainingMockMvc
            .perform(delete(ENTITY_API_URL_ID, botTraining.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return botTrainingRepository.count();
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

    protected BotTraining getPersistedBotTraining(BotTraining botTraining) {
        return botTrainingRepository.findById(botTraining.getId()).orElseThrow();
    }

    protected void assertPersistedBotTrainingToMatchAllProperties(BotTraining expectedBotTraining) {
        assertBotTrainingAllPropertiesEquals(expectedBotTraining, getPersistedBotTraining(expectedBotTraining));
    }

    protected void assertPersistedBotTrainingToMatchUpdatableProperties(BotTraining expectedBotTraining) {
        assertBotTrainingAllUpdatablePropertiesEquals(expectedBotTraining, getPersistedBotTraining(expectedBotTraining));
    }
}
