package io.akhilennu.chatbot.service;

import io.akhilennu.chatbot.domain.ChatMessage;
import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.management.SessionManager;
import io.akhilennu.chatbot.repository.ChatMessageRepository;
import io.akhilennu.chatbot.service.dto.ChatMessageDTO;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDetailsRequestDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDetailsResponseDTO;
import io.akhilennu.chatbot.service.handler.IntentHandler;
import io.akhilennu.chatbot.service.mapper.ChatMessageMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service Implementation for managing {@link io.akhilennu.chatbot.domain.ChatMessage}.
 */
@Service
@Transactional
public class ChatMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    private ApplicationContext applicationContext;

    private SessionManager sessionManager;

    private IntentService intentService;

    private Map<String, String> hardcodedChecks = new HashMap<>();

    public ChatMessageService(
        ChatMessageRepository chatMessageRepository,
        ChatMessageMapper chatMessageMapper,
        ApplicationContext applicationContext,
        SessionManager sessionManager,
        IntentService intentService
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
        this.applicationContext = applicationContext;
        this.sessionManager = sessionManager;
        this.intentService = intentService;

        hardcodedChecks.put("EXIT", "Current intent flow is exitted");
    }

    /**
     * Save a chatMessage.
     *
     * @param chatMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {
        LOG.debug("Request to save ChatMessage : {}", chatMessageDTO);
        ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
        chatMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toDto(chatMessage);
    }

    /**
     * Update a chatMessage.
     *
     * @param chatMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatMessageDTO update(ChatMessageDTO chatMessageDTO) {
        LOG.debug("Request to update ChatMessage : {}", chatMessageDTO);
        ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
        chatMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toDto(chatMessage);
    }

    /**
     * Partially update a chatMessage.
     *
     * @param chatMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatMessageDTO> partialUpdate(ChatMessageDTO chatMessageDTO) {
        LOG.debug("Request to partially update ChatMessage : {}", chatMessageDTO);

        return chatMessageRepository
            .findById(chatMessageDTO.getId())
            .map(existingChatMessage -> {
                chatMessageMapper.partialUpdate(existingChatMessage, chatMessageDTO);

                return existingChatMessage;
            })
            .map(chatMessageRepository::save)
            .map(chatMessageMapper::toDto);
    }

    /**
     * Get all the chatMessages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> findAll() {
        LOG.debug("Request to get all ChatMessages");
        return chatMessageRepository.findAll().stream().map(chatMessageMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one chatMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatMessageDTO> findOne(Long id) {
        LOG.debug("Request to get ChatMessage : {}", id);
        return chatMessageRepository.findById(id).map(chatMessageMapper::toDto);
    }

    /**
     * Delete the chatMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ChatMessage : {}", id);
        chatMessageRepository.deleteById(id);
    }

    public ResponseDataDTO chatWithBot(Long botId, String message, String sessionId) {
        Map<String, String> sessionContext = sessionManager.getSessionMap(sessionId);
        if (hardcodedChecks.containsKey(message)) {
            sessionContext.remove("ACTIVE_INTENT");
            return respondWith(hardcodedChecks.get(message));
        }
        UtteranceDetailsResponseDTO response = getUtteranceDetails(botId, message);

        String activeIntent = null;
        if (sessionContext.containsKey("ACTIVE_INTENT")) {
            activeIntent = sessionContext.get("ACTIVE_INTENT");
        }
        double confidence = response.getIntent().getConfidence();
        String predictedIntent = response.getIntent().getLabel();
        if (Objects.isNull(activeIntent)) {
            activeIntent = predictedIntent;
        }
        //TODO: Values are hardcoded for now, make them generic
        if (!predictedIntent.equals(activeIntent) && confidence > 0.85) {
            return respondWith(
                "You are yet to provide all details for " +
                activeIntent +
                " but looks like you are trying to switch conversation to " +
                predictedIntent +
                " please type EXIT to exit the current flow and then try again"
            );
        } else if (confidence < 0.85) {
            predictedIntent = activeIntent; // Simply ignoring the predicted intent
        }
        Intent intentDetails = intentService.getIntentDetailsByName(predictedIntent);
        // Try to get <intent>_handler bean first
        String handlerBeanName = predictedIntent + "_handler";
        IntentHandler handler = null;
        if (applicationContext.containsBean(handlerBeanName)) {
            handler = (IntentHandler) applicationContext.getBean(handlerBeanName);
        } else {
            String intentResponseTypeHandler = intentDetails.getRespType() + "_handler";
            if (applicationContext.containsBean(intentResponseTypeHandler)) {
                handler = (IntentHandler) applicationContext.getBean(intentResponseTypeHandler);
            } else {
                handler = (IntentHandler) applicationContext.getBean("DIRECT_RESPONSE_handler");
            }
        }

        return handler.processUserIntent(botId.toString(), message, response, intentDetails, sessionContext);
    }

    private ResponseDataDTO respondWith(String string) {
        ResponseDataDTO response = new ResponseDataDTO();
        response.setChannelName("web");
        response.setType("default");
        response.setContent(string);
        return response;
    }

    public UtteranceDetailsResponseDTO getUtteranceDetails(Long botId, String sentence) {
        String url = "http://localhost:5000/getUtteranceDetails";

        RestTemplate restTemplate = new RestTemplate();

        UtteranceDetailsRequestDTO request = new UtteranceDetailsRequestDTO();
        request.setBotId(botId);
        request.setSentence(sentence);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UtteranceDetailsRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<UtteranceDetailsResponseDTO> response = restTemplate.postForEntity(url, entity, UtteranceDetailsResponseDTO.class);

        return response.getBody();
    }
}
