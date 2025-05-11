package com.ufftcc.boraestudar.discord.configuration;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfiguration {

    @Value("${discord.token}")
    private String TOKEN;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {

        GatewayDiscordClient gateway = DiscordClientBuilder.create(TOKEN)
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .login()
                .block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            if (event.getMessage().getContent().equalsIgnoreCase("!ping")) {
                event.getMessage().getChannel().block()
                        .createMessage("Pong!").block();
            }
        });

        return gateway;
    }

}