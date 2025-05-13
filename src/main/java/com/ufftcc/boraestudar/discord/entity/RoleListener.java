package com.ufftcc.boraestudar.discord.entity;

import discord4j.common.util.Snowflake;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.CategoryCreateMono;
import discord4j.core.spec.GuildEditMono;
import discord4j.rest.util.PermissionSet;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.List;

import static discord4j.rest.util.Permission.VIEW_CHANNEL;
import static java.lang.System.out;

public abstract class RoleListener {

    @Value("${discord.guildId}")
    private String guildId;

    public Mono<Void> processCommand(Role role) {

        out.println("Criando a Categoria . . .");
        out.println("Nome: " + role.getName());

        PermissionSet viewCategory = PermissionSet.of(VIEW_CHANNEL);
        PermissionSet disableCategory = PermissionSet.none();
        disableCategory.removeAll(viewCategory);

        GuildEditMono guild = role.getGuild().block().edit();

        out.println("id do Cargo @everyone: " + Snowflake.of(guildId));
        out.println("id do Cargo "+role.getName()+": " + role.getId());

        CategoryCreateMono category = guild
                .block()
                .createCategory(role.getName())
                .withPermissionOverwrites(
                        PermissionOverwrite
                                .forRole(Snowflake.of(guildId), disableCategory, viewCategory),
                        PermissionOverwrite
                                .forRole(role.getId(), viewCategory, disableCategory));
        /*
         * List<PermissionOverwrite> po = category.permissionOverwrites().get();
         * 
         * for (PermissionOverwrite x : po) {
         * out.println("PermissionOverwrite x: " + x.toString());
         * }
         */
        return category.then();

    }
}