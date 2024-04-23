package dev.langchain4j.example.chat;

import static java.time.Duration.ofSeconds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AssistantFactory {

    @Inject
    @ConfigProperty(name = "hugging.face.api.key")
    private String HUGGING_FACE_API_KEY;

    @Inject
    @ConfigProperty(name = "chat.model.id")
    private String CHAT_MODEL_ID;

    @Inject
    @ConfigProperty(name = "chat.model.timeout")
    private Integer TIMEOUT;

    @Inject
    @ConfigProperty(name = "chat.model.max.token")
    private Integer MAX_NEW_TOKEN;

    @Inject
    @ConfigProperty(name = "chat.model.temperature")
    private Double TEMPERATURE;

    @Inject
    @ConfigProperty(name = "chat.memory.max.messages")
    private Integer MAX_MESSAGES;

    interface Assistant {
        String chat(@MemoryId String sessionId, @UserMessage String userMessage);
     }

    @Produces
    public Assistant getChatAssistant() {

        HuggingFaceChatModel model = HuggingFaceChatModel.builder()
                .accessToken(HUGGING_FACE_API_KEY)
                .modelId(CHAT_MODEL_ID)
                .timeout(ofSeconds(TIMEOUT))
                .temperature(TEMPERATURE)
                .maxNewTokens(MAX_NEW_TOKEN)
                .waitForModel(true)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(
                        sessionId -> MessageWindowChatMemory.withMaxMessages(MAX_MESSAGES))
                .build();

    }

}
