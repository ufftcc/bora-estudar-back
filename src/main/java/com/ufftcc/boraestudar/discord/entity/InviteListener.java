package com.ufftcc.boraestudar.discord.entity;

import discord4j.core.object.Invite;
import reactor.core.publisher.Mono;

public abstract class InviteListener {
    public Mono<Void> processCommand(Invite invite) {

        System.out.println("Objeto Invite: ");
        System.out.println(invite.toString());

        return Mono.empty();
    }
}