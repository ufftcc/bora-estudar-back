package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.MemberListener;
import discord4j.core.event.domain.guild.MemberUpdateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class MemberUpdateListener extends MemberListener implements EventListener<MemberUpdateEvent> {

    String roleId = "1242374238728491010";

    @Override
    public Class<MemberUpdateEvent> getEventType() {
        return MemberUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberUpdateEvent event) {
        out.println("Member " + event.getMember().block().getUsername() + " atualizou com sucesso . . .");
        out.println("getMemberData()");
        out.println(event.getMember().block().getMemberData());
        out.println("getUserData()");
        out.println(event.getMember().block().getUserData());

        return processCommand(event.getMember().block());
    }
}