package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.VoiceChannelListener;
import discord4j.core.event.domain.channel.VoiceChannelCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class VoiceChannelCreateListener extends VoiceChannelListener implements EventListener<VoiceChannelCreateEvent> {

    @Override
    public Class<VoiceChannelCreateEvent> getEventType() {
        return VoiceChannelCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(VoiceChannelCreateEvent event) {
        out.println("Canal de Voz criado com sucesso: " + event.getChannel().getName());
        return processCommand(event.getChannel());
    }
}