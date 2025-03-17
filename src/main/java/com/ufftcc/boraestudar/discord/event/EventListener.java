package com.ufftcc.boraestudar.discord.event;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

public interface EventListener<T extends Event> {

    Class<T> getEventType();
    Mono<Void> execute(T event);

    default Mono<Void> handleError(Throwable error) {
        out.println("Unable to process " + getEventType().getSimpleName() + "error: " + error);
        return Mono.empty();
    }
}