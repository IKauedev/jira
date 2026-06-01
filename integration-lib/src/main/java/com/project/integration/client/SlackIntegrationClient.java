package com.project.integration.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class SlackIntegrationClient extends BaseIntegrationClient {

    public SlackIntegrationClient(WebClient webClient) {
        super(webClient);
    }

    public Mono<SlackMessage> sendMessage(String channel, String message) {
        SlackMessageRequest request = SlackMessageRequest.builder()
                .channel(channel)
                .text(message)
                .build();

        return post("", request, SlackMessage.class);
    }

    public Mono<SlackUser> getUser(String userId) {
        return get("/users.info?user=" + userId, SlackUser.class);
    }

    public Mono<SlackChannel> getChannel(String channelId) {
        return get("/conversations.info?channel=" + channelId, SlackChannel.class);
    }

    @Data
    @Builder
    public static class SlackMessageRequest {
        private String channel;
        private String text;
    }

    @Data
    @Builder
    public static class SlackMessage {
        private boolean ok;
        private String channel;
        private String ts;
        private String message;
    }

    @Data
    @Builder
    public static class SlackUser {
        private boolean ok;
        private User user;

        @Data
        public static class User {
            private String id;
            private String name;
            private String realName;
            private String email;
        }
    }

    @Data
    @Builder
    public static class SlackChannel {
        private boolean ok;
        private Channel channel;

        @Data
        public static class Channel {
            private String id;
            private String name;
            private String topic;
        }
    }
}
