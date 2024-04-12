package it.dev.langchain4j.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.server.ServerCdiExtension;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;

@HelidonTest
public class ChatServiceTest {

    private static CountDownLatch countDown;

    @Inject
    private ServerCdiExtension server;

    @Test
    public void testChat() throws Exception {
        countDown = new CountDownLatch(1);
        URI uri = new URI(String.format("ws://localhost:%d/chat", server.port()));
        ChatClient client = new ChatClient(uri);
        client.sendMessage("When was langchain4j launched?");
        countDown.await(120, TimeUnit.SECONDS);
        client.close();
    }

    // Called by ChatClient.onMessage
    public static void verify(String message) {
        assertNotNull(message);
        assertTrue(message.contains("2023"), message);
        countDown.countDown();
    }

}
