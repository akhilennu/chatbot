package io.akhilennu.chatbot.web.rest;

import static io.akhilennu.chatbot.domain.SlotValueAsserts.*;
import static io.akhilennu.chatbot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.akhilennu.chatbot.IntegrationTest;
import io.akhilennu.chatbot.domain.SlotValue;
import io.akhilennu.chatbot.repository.SlotValueRepository;
import io.akhilennu.chatbot.service.dto.SlotValueDTO;
import io.akhilennu.chatbot.service.mapper.SlotValueMapper;
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
 * Integration tests for the {@link SlotValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SlotValueResourceIT {

    private static final String DEFAULT_SLOT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SLOT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLOT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_SLOT_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/slot-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SlotValueRepository slotValueRepository;

    @Autowired
    private SlotValueMapper slotValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlotValueMockMvc;

    private SlotValue slotValue;

    private SlotValue insertedSlotValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotValue createEntity() {
        return new SlotValue().slotName(DEFAULT_SLOT_NAME).slotValue(DEFAULT_SLOT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotValue createUpdatedEntity() {
        return new SlotValue().slotName(UPDATED_SLOT_NAME).slotValue(UPDATED_SLOT_VALUE);
    }

    @BeforeEach
    void initTest() {
        slotValue = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSlotValue != null) {
            slotValueRepository.delete(insertedSlotValue);
            insertedSlotValue = null;
        }
    }

    @Test
    @Transactional
    void createSlotValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);
        var returnedSlotValueDTO = om.readValue(
            restSlotValueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotValueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SlotValueDTO.class
        );

        // Validate the SlotValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSlotValue = slotValueMapper.toEntity(returnedSlotValueDTO);
        assertSlotValueUpdatableFieldsEquals(returnedSlotValue, getPersistedSlotValue(returnedSlotValue));

        insertedSlotValue = returnedSlotValue;
    }

    @Test
    @Transactional
    void createSlotValueWithExistingId() throws Exception {
        // Create the SlotValue with an existing ID
        slotValue.setId(1L);
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSlotNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotValue.setSlotName(null);

        // Create the SlotValue, which fails.
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        restSlotValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotValueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlotValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotValue.setSlotValue(null);

        // Create the SlotValue, which fails.
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        restSlotValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotValueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSlotValues() throws Exception {
        // Initialize the database
        insertedSlotValue = slotValueRepository.saveAndFlush(slotValue);

        // Get all the slotValueList
        restSlotValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slotValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].slotName").value(hasItem(DEFAULT_SLOT_NAME)))
            .andExpect(jsonPath("$.[*].slotValue").value(hasItem(DEFAULT_SLOT_VALUE)));
    }

    @Test
    @Transactional
    void getSlotValue() throws Exception {
        // Initialize the database
        insertedSlotValue = slotValueRepository.saveAndFlush(slotValue);

        // Get the slotValue
        restSlotValueMockMvc
            .perform(get(ENTITY_API_URL_ID, slotValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slotValue.getId().intValue()))
            .andExpect(jsonPath("$.slotName").value(DEFAULT_SLOT_NAME))
            .andExpect(jsonPath("$.slotValue").value(DEFAULT_SLOT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingSlotValue() throws Exception {
        // Get the slotValue
        restSlotValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSlotValue() throws Exception {
        // Initialize the database
        insertedSlotValue = slotValueRepository.saveAndFlush(slotValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotValue
        SlotValue updatedSlotValue = slotValueRepository.findById(slotValue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSlotValue are not directly saved in db
        em.detach(updatedSlotValue);
        updatedSlotValue.slotName(UPDATED_SLOT_NAME).slotValue(UPDATED_SLOT_VALUE);
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(updatedSlotValue);

        restSlotValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slotValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSlotValueToMatchAllProperties(updatedSlotValue);
    }

    @Test
    @Transactional
    void putNonExistingSlotValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotValue.setId(longCount.incrementAndGet());

        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slotValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSlotValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotValue.setId(longCount.incrementAndGet());

        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSlotValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotValue.setId(longCount.incrementAndGet());

        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSlotValueWithPatch() throws Exception {
        // Initialize the database
        insertedSlotValue = slotValueRepository.saveAndFlush(slotValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotValue using partial update
        SlotValue partialUpdatedSlotValue = new SlotValue();
        partialUpdatedSlotValue.setId(slotValue.getId());

        partialUpdatedSlotValue.slotName(UPDATED_SLOT_NAME).slotValue(UPDATED_SLOT_VALUE);

        restSlotValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlotValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlotValue))
            )
            .andExpect(status().isOk());

        // Validate the SlotValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotValueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSlotValue, slotValue),
            getPersistedSlotValue(slotValue)
        );
    }

    @Test
    @Transactional
    void fullUpdateSlotValueWithPatch() throws Exception {
        // Initialize the database
        insertedSlotValue = slotValueRepository.saveAndFlush(slotValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotValue using partial update
        SlotValue partialUpdatedSlotValue = new SlotValue();
        partialUpdatedSlotValue.setId(slotValue.getId());

        partialUpdatedSlotValue.slotName(UPDATED_SLOT_NAME).slotValue(UPDATED_SLOT_VALUE);

        restSlotValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlotValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlotValue))
            )
            .andExpect(status().isOk());

        // Validate the SlotValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotValueUpdatableFieldsEquals(partialUpdatedSlotValue, getPersistedSlotValue(partialUpdatedSlotValue));
    }

    @Test
    @Transactional
    void patchNonExistingSlotValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotValue.setId(longCount.incrementAndGet());

        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, slotValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSlotValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotValue.setId(longCount.incrementAndGet());

        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSlotValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotValue.setId(longCount.incrementAndGet());

        // Create the SlotValue
        SlotValueDTO slotValueDTO = slotValueMapper.toDto(slotValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotValueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(slotValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SlotValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSlotValue() throws Exception {
        // Initialize the database
        insertedSlotValue = slotValueRepository.saveAndFlush(slotValue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the slotValue
        restSlotValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, slotValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return slotValueRepository.count();
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

    protected SlotValue getPersistedSlotValue(SlotValue slotValue) {
        return slotValueRepository.findById(slotValue.getId()).orElseThrow();
    }

    protected void assertPersistedSlotValueToMatchAllProperties(SlotValue expectedSlotValue) {
        assertSlotValueAllPropertiesEquals(expectedSlotValue, getPersistedSlotValue(expectedSlotValue));
    }

    protected void assertPersistedSlotValueToMatchUpdatableProperties(SlotValue expectedSlotValue) {
        assertSlotValueAllUpdatablePropertiesEquals(expectedSlotValue, getPersistedSlotValue(expectedSlotValue));
    }
}
