package com.ufftcc.boraestudar.discord.entity;

import discord4j.common.util.Snowflake;
import discord4j.core.object.ExtendedPermissionOverwrite;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.spec.TextChannelCreateMono;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.Set;

import static java.lang.System.out;

public abstract class CategoryListener {

    @Value("${discord.guildId}")
    private String guildId;

    public Mono<Void> processCommand(Category category) {

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

        String textChannelName = roleClassCode.concat("-text-").concat(roleIndex).toLowerCase();

        out.println("Criando o Canal de Texto . . .");
        TextChannelCreateMono textChannel = category.getGuild().block().createTextChannel(textChannelName);
        out.println("Associando o Canal de Texto: " + textChannelName + " à Categoria: " + category.getName());
        textChannel = textChannel.withParentId(category.getId());

        return textChannel.then();
    }
}