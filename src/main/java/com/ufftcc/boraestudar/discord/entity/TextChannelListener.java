package com.ufftcc.boraestudar.discord.entity;

import discord4j.common.util.Snowflake;
import discord4j.core.object.ExtendedPermissionOverwrite;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.VoiceChannelCreateMono;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.Set;

import static java.lang.System.out;

public abstract class TextChannelListener {

    @Value("${discord.guildId}")
    private String guildId;

    public Mono<Void> processCommand(TextChannel textChannel) {

        Category category = textChannel.getCategory().block();

        Set<ExtendedPermissionOverwrite> l = category.getPermissionOverwrites();

        Snowflake roleId = null;
        Role role = null;
        for (PermissionOverwrite x : l) {
            if (x.getRoleId().get().compareTo(Snowflake.of(guildId)) != 0) {
                roleId = x.getRoleId().get();
                role = category.getGuild().block().getRoleById(roleId).block();
            }
        }

        String roleName         = role.getName();
        String roleClassCode    = roleName.substring(0, 8);
        String[] parts          = roleName.split("-");
        String roleIndex        = parts[2];

        String voiceChannelName = roleClassCode.concat("-voice-").concat(roleIndex).toLowerCase();

        out.println("Criando o Canal de Voz . . .");
        VoiceChannelCreateMono voiceChannel = category.getGuild().block().createVoiceChannel(voiceChannelName);
        out.println("Associando o Canal de Voz: " + voiceChannelName + " à Categoria: " + category.getName());
        voiceChannel = voiceChannel.withParentId(category.getId());

        return voiceChannel.then();
    }
}