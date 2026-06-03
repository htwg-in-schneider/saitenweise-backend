package de.htwg.in.schneider.saitenweise.backend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.htwg.in.schneider.saitenweise.backend.clientmodel.ChatCompletionsRequest;
import de.htwg.in.schneider.saitenweise.backend.clientmodel.ChatCompletionsResponse;
import de.htwg.in.schneider.saitenweise.backend.clientmodel.ChatMessage;
import de.htwg.in.schneider.saitenweise.backend.controller.dto.AiSuggestionResponse;
import de.htwg.in.schneider.saitenweise.backend.model.Product;

@Service
public class AiSuggestionService {

    private static final Logger LOG = LoggerFactory.getLogger(AiSuggestionService.class);

    @Value("${ai.suggestion.api-url}")
    private String aiApiUrl;

    @Value("${ai.suggestion.api-key}")
    private String aiApiKey;

    @Value("${ai.suggestion.model}")
    private String aiModel;

    @Value("${ai.suggestion.timeout-seconds}")
    private int aiTimeoutSeconds;

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiSuggestionResponse getSuggestion(Product product) {
        String prompt = loadPrompt();

        String promptWithProduct = prompt
                .replace("{title}", product.getTitle())
                .replace("{category}", product.getCategory().getGermanName())
                .replace("{description}", product.getDescription())
                .replace("{price}", String.valueOf(product.getPrice()));

        ChatCompletionsRequest request = buildRequest(promptWithProduct);

        try {
            String requestBody = objectMapper.writeValueAsString(request);

            String rawResponse = webClient.post()
                    .uri(aiApiUrl)
                    .header("Authorization", "Bearer " + aiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(aiTimeoutSeconds));

            LOG.info("Raw AI response: {}", rawResponse);

            ChatCompletionsResponse apiResponse = objectMapper.readValue(rawResponse, ChatCompletionsResponse.class);
            return objectMapper.readValue(
                    apiResponse.getChoices().get(0).getMessage().getContent(), AiSuggestionResponse.class);

        } catch (WebClientResponseException e) {
            LOG.error("AI API error: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("AI service unavailable", e);
        } catch (Exception e) {
            LOG.error("AI call failed for product {}", product.getId(), e);
            throw new RuntimeException("AI service unavailable", e);
        }
    }

    private ChatCompletionsRequest buildRequest(String promptWithProduct) {
        ChatCompletionsRequest request = new ChatCompletionsRequest();
        request.setModel(aiModel);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent(promptWithProduct);
        request.setMessages(java.util.List.of(chatMessage));

        ChatCompletionsRequest.ResponseFormat responseFormat = new ChatCompletionsRequest.ResponseFormat();
        responseFormat.setType("json_object");
        request.setResponseFormat(responseFormat);

        return request;
    }

    private String loadPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("ai-suggestion-prompt.txt");
            return java.nio.file.Files.readString(resource.getFile().toPath());
        } catch (IOException e) {
            LOG.error("Failed to load prompt template", e);
            throw new IllegalStateException("Prompt file 'ai-suggestion-prompt.txt' not found in classpath", e);
        }
    }
}
