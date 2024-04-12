package it.dev.langchain4j.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

@HelidonTest
public class ModelResourceTest {

    private static final String APP_URL = "/api/model/";

    @Inject
    private WebTarget target;

    private static String appUrl(String path) {
        return APP_URL + path;
    }

    @Test
    public void testLanguageMode() {
        String answer = target.path(appUrl("language"))
                .queryParam("question", "When was langchain4j launched?")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);

        // Typical hallucination: langchain4j was launched in 2023!
        assertTrue(answer.contains("2017"), "actual: " + answer);
    }

    @Test
    public void testChatMode() {
        String answer = target.path(appUrl("chat"))
                .queryParam("userMessage", "Which are the most used Large Language Models?")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        assertTrue(answer.contains("BERT"), "actual: " + answer);
    }

    @Test
    public void testEmbeddingMode() {
 
        JsonObject answer = target.path(appUrl("similarity"))
                .queryParam("text1", "I like Jarkata EE and MicroProfile.")
                .queryParam("text2", "I like Python language.")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        double score = answer.getJsonNumber("relevance-score").doubleValue();
        assertTrue(score > 0.69 && score < 0.70, "actual score: " + score);

        double similarity = answer.getJsonNumber("similarity").doubleValue();
        assertTrue(similarity > 0.38 && similarity < 0.39,
                "actual similarity: " + similarity);
    }

}
