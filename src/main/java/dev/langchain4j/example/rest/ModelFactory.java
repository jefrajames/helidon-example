package dev.langchain4j.example.rest;

import static dev.langchain4j.model.huggingface.HuggingFaceModelName.SENTENCE_TRANSFORMERS_ALL_MINI_LM_L6_V2;
import static dev.langchain4j.model.huggingface.HuggingFaceModelName.TII_UAE_FALCON_7B_INSTRUCT;
import static java.time.Duration.ofSeconds;

import java.time.Duration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ModelFactory {

    @Inject
    @ConfigProperty(name = "hugging.face.api.key")
    String HUGGING_FACE_API_KEY;

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

    @Produces
    @LanguageModel
    // Warning: LanguageModel deprecated in favor of ChatLanguageModel
    public HuggingFaceChatModel getLanguageModel() {

        return HuggingFaceChatModel.builder()
                .accessToken(HUGGING_FACE_API_KEY)
                .modelId(CHAT_MODEL_ID)
                .timeout(Duration.ofSeconds(TIMEOUT))
                .temperature(TEMPERATURE)
                .maxNewTokens(MAX_NEW_TOKEN)
                .waitForModel(true)
                .build();
    }

    @Produces
    @EmbeddingModel
    private HuggingFaceEmbeddingModel getEmbeddingModel() {
        return HuggingFaceEmbeddingModel.builder()
                .accessToken(HUGGING_FACE_API_KEY)
                .modelId(SENTENCE_TRANSFORMERS_ALL_MINI_LM_L6_V2)
                .timeout(Duration.ofSeconds(120))
                .waitForModel(true)
                .build();
    }

    // Returns an 'instruct' model fine-tuned to follow prompted instructions
    @Produces
    @ChatModel
    public HuggingFaceChatModel getChatModel() {

        return HuggingFaceChatModel.builder()
                .accessToken(HUGGING_FACE_API_KEY)
                .modelId(TII_UAE_FALCON_7B_INSTRUCT)
                .timeout(ofSeconds(120))
                .temperature(1.0)
                .maxNewTokens(200)
                .waitForModel(true)
                .build();

    }

}
