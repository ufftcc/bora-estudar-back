package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.InviteListener;
import discord4j.core.event.domain.InviteCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class InviteCreateListener extends InviteListener implements EventListener<InviteCreateEvent> {

    String roleId = "1242374238728491010";

    @Override
    public Class<InviteCreateEvent> getEventType() {
        return InviteCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(InviteCreateEvent event) {
        out.println("Invite " + event.getInvite().block().getCode() + " criado com sucesso . . .");
        return processCommand(event.getInvite().block());
    }
}
