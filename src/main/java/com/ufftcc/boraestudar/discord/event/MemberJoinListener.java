package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.MemberListener;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class MemberJoinListener extends MemberListener implements EventListener<MemberJoinEvent> {

    String roleId = "1242374238728491010";

    @Override
    public Class<MemberJoinEvent> getEventType() {
        return MemberJoinEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberJoinEvent event) {
        out.println("Member " + event.getMember().getUsername() + " entrou com sucesso . . .");

        return processCommand(event.getMember());
    }
}