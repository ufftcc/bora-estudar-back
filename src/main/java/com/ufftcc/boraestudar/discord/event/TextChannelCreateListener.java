package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.TextChannelListener;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class TextChannelCreateListener extends TextChannelListener implements EventListener<TextChannelCreateEvent> {

    @Override
    public Class<TextChannelCreateEvent> getEventType() {
        return TextChannelCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(TextChannelCreateEvent event) {
        out.println("Canal de Texto criado com sucesso: " + event.getChannel().getName());
        return processCommand(event.getChannel());

    }
}