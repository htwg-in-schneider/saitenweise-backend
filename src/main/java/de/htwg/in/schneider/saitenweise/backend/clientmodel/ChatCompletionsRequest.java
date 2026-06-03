package de.htwg.in.schneider.saitenweise.backend.clientmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ChatCompletionsRequest {
    private String model;
    private List<ChatMessage> messages;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    public static class ResponseFormat {
        @JsonProperty("type")
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
