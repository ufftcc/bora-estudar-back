package com.ufftcc.boraestudar.discord.entity;

import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public abstract class MemberListener {
    public Mono<Void> processCommand(Member member) {

        System.out.println(member.getRoles());
        System.out.println(member);

        return Mono.empty();
    }
}