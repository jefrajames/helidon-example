package dev.langchain4j.example.rest;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.data.segment.TextSegment.textSegment;
import static dev.langchain4j.store.embedding.CosineSimilarity.between;
import static dev.langchain4j.store.embedding.RelevanceScore.fromCosineSimilarity;

import java.util.List;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import org.eclipse.microprofile.openapi.annotations.Operation;

@ApplicationScoped
@Path("model")
public class ModelResource {

    @Inject
    @LanguageModel
    private HuggingFaceChatModel languageModel;

    @Inject
    @EmbeddingModel
    private HuggingFaceEmbeddingModel embeddingModel;

    @Inject
    @ChatModel
    private HuggingFaceChatModel chatModel;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("language")
    @Operation(summary = "Use the language model.", description = "Provide a sequence of words to a large language model.", operationId = "languageModelAsk")
    public String languageModelAsk(@QueryParam("question") String question) {

        String answer;
        try {
            answer = languageModel.generate(question);
        } catch (Exception e) {
            answer = "My failure reason is:\n\n" + e.getMessage();
        }

        return answer;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("chat")
    @Operation(summary = "Use the chat model.", description = "Assume you are talking with an agent that is knowledgeable about "
            +
            "Large Language Models. Ask any question about it.", operationId = "chatModelAsk")
    public List<String> chatModelAsk(@QueryParam("userMessage") String userMessage) {

        SystemMessage systemMessage = systemMessage(
                "You are very knowledgeble about Large Language Models. Be friendly. Give concise answers.");

        AiMessage aiMessage = chatModel.generate(
                systemMessage,
                userMessage(userMessage)).content();

        return List.of(
                "System: " + systemMessage.text(),
                "Me:     " + userMessage,
                "Agent:  " + aiMessage.text().trim());
    }

    private Properties getProperties(String value, Embedding embedding) {
        Properties p = new Properties();
        p.put("words", value.split(" "));
        p.put("embedding-vector", embedding.vectorAsList());
        return p;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("similarity")
    @Operation(summary = "Use the embedding model.", description = "Determine the similarity and relevance score of two sentences.", operationId = "similarity")
    public Properties similarity(
            @QueryParam("text1") String text1,
            @QueryParam("text2") String text2) {

        // HuggingFaceEmbeddingModel model = getEmbeddingModel();

        List<TextSegment> textSegments = List.of(textSegment(text1), textSegment(text2));
        List<Embedding> embeddings = embeddingModel.embedAll(textSegments).content();
        double similarity = between(embeddings.get(0), embeddings.get(1));

        Properties p = new Properties();
        p.put("text1", getProperties(text1, embeddings.get(0)));
        p.put("text2", getProperties(text2, embeddings.get(1)));
        p.put("similarity", similarity);
        p.put("relevance-score", fromCosineSimilarity(similarity));

        return p;
    }
}
