package com.ufftcc.boraestudar.discord.entity;

import discord4j.core.object.ExtendedInvite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.InviteCreateMono;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.System.out;

public abstract class VoiceChannelListener {
    public Mono<Void> processCommand(VoiceChannel voiceChannel) {

        InviteCreateMono invite = voiceChannel.createInvite()
                .withMaxAge(Integer.valueOf(0))
                .withMaxUses(Integer.valueOf(0))
                .withTemporary(Boolean.FALSE);

        out.println("Criando o Convite para o Canal . . .");
        out.println(invite);

        Guild guild = voiceChannel.getGuild().block();
        List<ExtendedInvite> g = guild.getInvites().collectList().block();

        out.println("Lista Extended Invite . . .");
        for (ExtendedInvite x : g) {
            out.println(x.toString());
            out.println("...");
        }

        List<Member> h = guild.getMembers().collectList().block();
        out.println("Lista Members . . .");
        for (Member y : h) {
            out.println(y.toString());
            out.println("...");
        }

        List<Member> i = guild.requestMembers().collectList().block();
        out.println("Lista Members 2 . . .");
        for (Member z : i) {
            out.println(z.toString());
            out.println("...");
        }


        //InviteData.builder();
        //ImmutableInviteData imutableData = null;
        //Invite invite = new Invite(voiceChannel.getClient(), imutableData );

        return invite.then();
    }
}
