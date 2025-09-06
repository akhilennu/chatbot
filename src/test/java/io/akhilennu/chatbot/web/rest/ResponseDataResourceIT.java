package io.akhilennu.chatbot.web.rest;

import static io.akhilennu.chatbot.domain.ResponseDataAsserts.*;
import static io.akhilennu.chatbot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.akhilennu.chatbot.IntegrationTest;
import io.akhilennu.chatbot.domain.ResponseData;
import io.akhilennu.chatbot.repository.ResponseDataRepository;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.mapper.ResponseDataMapper;
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
 * Integration tests for the {@link ResponseDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResponseDataResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/response-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResponseDataRepository responseDataRepository;

    @Autowired
    private ResponseDataMapper responseDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResponseDataMockMvc;

    private ResponseData responseData;

    private ResponseData insertedResponseData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponseData createEntity() {
        return new ResponseData().type(DEFAULT_TYPE).content(DEFAULT_CONTENT).channelName(DEFAULT_CHANNEL_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponseData createUpdatedEntity() {
        return new ResponseData().type(UPDATED_TYPE).content(UPDATED_CONTENT).channelName(UPDATED_CHANNEL_NAME);
    }

    @BeforeEach
    void initTest() {
        responseData = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedResponseData != null) {
            responseDataRepository.delete(insertedResponseData);
            insertedResponseData = null;
        }
    }

    @Test
    @Transactional
    void createResponseData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);
        var returnedResponseDataDTO = om.readValue(
            restResponseDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responseDataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResponseDataDTO.class
        );

        // Validate the ResponseData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResponseData = responseDataMapper.toEntity(returnedResponseDataDTO);
        assertResponseDataUpdatableFieldsEquals(returnedResponseData, getPersistedResponseData(returnedResponseData));

        insertedResponseData = returnedResponseData;
    }

    @Test
    @Transactional
    void createResponseDataWithExistingId() throws Exception {
        // Create the ResponseData with an existing ID
        responseData.setId(1L);
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponseDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responseDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResponseData() throws Exception {
        // Initialize the database
        insertedResponseData = responseDataRepository.saveAndFlush(responseData);

        // Get all the responseDataList
        restResponseDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responseData.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME)));
    }

    @Test
    @Transactional
    void getResponseData() throws Exception {
        // Initialize the database
        insertedResponseData = responseDataRepository.saveAndFlush(responseData);

        // Get the responseData
        restResponseDataMockMvc
            .perform(get(ENTITY_API_URL_ID, responseData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responseData.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.channelName").value(DEFAULT_CHANNEL_NAME));
    }

    @Test
    @Transactional
    void getNonExistingResponseData() throws Exception {
        // Get the responseData
        restResponseDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResponseData() throws Exception {
        // Initialize the database
        insertedResponseData = responseDataRepository.saveAndFlush(responseData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responseData
        ResponseData updatedResponseData = responseDataRepository.findById(responseData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResponseData are not directly saved in db
        em.detach(updatedResponseData);
        updatedResponseData.type(UPDATED_TYPE).content(UPDATED_CONTENT).channelName(UPDATED_CHANNEL_NAME);
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(updatedResponseData);

        restResponseDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responseDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responseDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResponseDataToMatchAllProperties(updatedResponseData);
    }

    @Test
    @Transactional
    void putNonExistingResponseData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responseData.setId(longCount.incrementAndGet());

        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponseDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responseDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responseDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponseData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responseData.setId(longCount.incrementAndGet());

        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponseDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responseDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponseData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responseData.setId(longCount.incrementAndGet());

        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponseDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responseDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResponseDataWithPatch() throws Exception {
        // Initialize the database
        insertedResponseData = responseDataRepository.saveAndFlush(responseData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responseData using partial update
        ResponseData partialUpdatedResponseData = new ResponseData();
        partialUpdatedResponseData.setId(responseData.getId());

        partialUpdatedResponseData.type(UPDATED_TYPE).channelName(UPDATED_CHANNEL_NAME);

        restResponseDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponseData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponseData))
            )
            .andExpect(status().isOk());

        // Validate the ResponseData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponseDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResponseData, responseData),
            getPersistedResponseData(responseData)
        );
    }

    @Test
    @Transactional
    void fullUpdateResponseDataWithPatch() throws Exception {
        // Initialize the database
        insertedResponseData = responseDataRepository.saveAndFlush(responseData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responseData using partial update
        ResponseData partialUpdatedResponseData = new ResponseData();
        partialUpdatedResponseData.setId(responseData.getId());

        partialUpdatedResponseData.type(UPDATED_TYPE).content(UPDATED_CONTENT).channelName(UPDATED_CHANNEL_NAME);

        restResponseDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponseData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponseData))
            )
            .andExpect(status().isOk());

        // Validate the ResponseData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponseDataUpdatableFieldsEquals(partialUpdatedResponseData, getPersistedResponseData(partialUpdatedResponseData));
    }

    @Test
    @Transactional
    void patchNonExistingResponseData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responseData.setId(longCount.incrementAndGet());

        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponseDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responseDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responseDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponseData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responseData.setId(longCount.incrementAndGet());

        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponseDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responseDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponseData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        responseData.setId(longCount.incrementAndGet());

        // Create the ResponseData
        ResponseDataDTO responseDataDTO = responseDataMapper.toDto(responseData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponseDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(responseDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponseData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResponseData() throws Exception {
        // Initialize the database
        insertedResponseData = responseDataRepository.saveAndFlush(responseData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the responseData
        restResponseDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, responseData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return responseDataRepository.count();
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

    protected ResponseData getPersistedResponseData(ResponseData responseData) {
        return responseDataRepository.findById(responseData.getId()).orElseThrow();
    }

    protected void assertPersistedResponseDataToMatchAllProperties(ResponseData expectedResponseData) {
        assertResponseDataAllPropertiesEquals(expectedResponseData, getPersistedResponseData(expectedResponseData));
    }

    protected void assertPersistedResponseDataToMatchUpdatableProperties(ResponseData expectedResponseData) {
        assertResponseDataAllUpdatablePropertiesEquals(expectedResponseData, getPersistedResponseData(expectedResponseData));
    }
}
