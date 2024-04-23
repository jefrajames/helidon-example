package dev.langchain4j.example.chat;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import dev.langchain4j.example.chat.AssistantFactory.Assistant;

@ApplicationScoped
public class ChatAgent {

    @Inject
    private Assistant assistant;  

    public String chat(String sessionId, String message) {
        return assistant.chat(sessionId, message).trim();
    }

}
