package org.example.todo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    String generation(@RequestParam("userInput") String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> stream(@RequestParam String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .stream()
                .content();
    }

    @GetMapping(value = "/chat/system", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> simplePromptExample(@RequestParam String userInput) {
        return this.chatClient.prompt()
                .system("You are a Todo tasks manager, you should only answer about Todo manage related questions." +
                        "If the user ask you about other things, you should answer you don't know. ")
                .user(userInput)
                .stream()
                .content();
    }
}