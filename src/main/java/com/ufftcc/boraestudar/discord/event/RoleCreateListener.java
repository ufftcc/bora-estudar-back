package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.RoleListener;
import discord4j.core.event.domain.role.RoleCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class RoleCreateListener extends RoleListener implements EventListener<RoleCreateEvent> {

    @Override
    public Class<RoleCreateEvent> getEventType() {
        return RoleCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(RoleCreateEvent event) {
        out.println("Cargo " + event.getRole().getName() + " criado com sucesso . . .");
        return processCommand(event.getRole());
    }
}
