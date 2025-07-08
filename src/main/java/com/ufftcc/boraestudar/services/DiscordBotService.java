package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.entities.StudyGroup;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.*;
import discord4j.core.spec.*;
import discord4j.core.spec.InviteCreateSpec;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.util.PermissionSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static discord4j.rest.util.Permission.*;

@Service
public class DiscordBotService {

    private static final Logger log = LoggerFactory.getLogger(DiscordBotService.class);
    @Value("${discord.token}")
    String token;
    @Value("${discord.guildId}")
    String guildId;

    private String generateClassName(StudyGroup studyGroup, StudyGroupCreateDto dto) {
        log.info("code {}", dto.getSubject().getCode());
        log.info("name {}", dto.getSubject().getName());
        return (dto.getSubject().getCode() + "-" +
                dto.getSubject().getName() + "-" +
                studyGroup.getId()).replace(" ","-");
    }

    private PermissionOverwrite[] getCategoryPermissionOverwrites(Role everyoneRole, RoleEditMono role) {

        PermissionSet allowPermissionsRole = PermissionSet.of(
                VIEW_CHANNEL,
                SEND_MESSAGES,  // Para canais de texto
                CONNECT,        // Para canais de voz
                SPEAK,           // Para canais de voz
                STREAM
        );

        PermissionSet disablePermissionsRole = PermissionSet.of(
                CREATE_INSTANT_INVITE
        );

        PermissionSet disablePermissionsEveryone = PermissionSet.of(
                VIEW_CHANNEL,
                SEND_MESSAGES,  // Para canais de texto
                CONNECT,        // Para canais de voz
                SPEAK,          // Para canais de voz
                CREATE_INSTANT_INVITE,
                STREAM
        );

        List<PermissionOverwrite> po = new ArrayList<>();
        po.add(PermissionOverwrite.forRole(everyoneRole.getId(), PermissionSet.none(), disablePermissionsEveryone));
        po.add(PermissionOverwrite.forRole(role.role().getId(), allowPermissionsRole, disablePermissionsRole));

        // Se precisar converter de volta para vetor
        PermissionOverwrite[] poArray = po.toArray(new PermissionOverwrite[0]);

        return poArray;
    }

    private Mono<Role> findEveryoneRole(Guild guild) {
        return guild.getRoles()
                .filter(role -> role.getName().equals("@everyone"))
                .next()
                .switchIfEmpty(Mono.error(new RuntimeException("Everyone role not found")));
    }

    private PermissionOverwrite[] getChannelPermissionOverwrites(Role everyoneRole, RoleEditMono role) {
        return getCategoryPermissionOverwrites(everyoneRole, role);
    }

    private Mono<String> createChannelsInCategory(String channelName, Guild guild,
                                                  CategoryEditMono category, RoleEditMono role) {

        // Cria o canal de texto (transforma Void em String vazia)
        Mono<String> textChannelOperation = createTextChannelInCategory(channelName, guild, category, role)
                .thenReturn("");

        // Cria o canal de voz (já retorna o invite)
        Mono<String> voiceChannelOperation = createVoiceChannelInCategory(channelName, guild, category, role);

        // Combina as operações e retorna apenas o invite
        return Mono.zip(textChannelOperation, voiceChannelOperation)
                .map(tuple -> {
                    // tuple.getT1() -> resultado do canal de texto (string vazia)
                    // tuple.getT2() -> invite do canal de voz
                    return tuple.getT2();
                })
                .doOnSuccess(invite -> log.info("Canais criados. Invite: {}", invite))
                .doOnError(e -> log.error("Falha na criação dos canais", e));
    }

    private Mono<Void> createTextChannelInCategory(String channelName, Guild guild, CategoryEditMono category, RoleEditMono role) {
        return findEveryoneRole(guild)
                .flatMap(everyoneRole -> guild.createTextChannel(channelName)
                        .withParentId(category.category().getId())
                        .withPermissionOverwrites(getChannelPermissionOverwrites(everyoneRole, role)))
                .then();
    }

    private Mono<String> createVoiceChannelInCategory(String channelName, Guild guild, CategoryEditMono category, RoleEditMono role) {
        return findEveryoneRole(guild)
                .flatMap(everyoneRole -> guild.createVoiceChannel(channelName)
                        .withParentId(category.category().getId())
                        .withPermissionOverwrites(getChannelPermissionOverwrites(everyoneRole, role))
                        .flatMap(voiceChannel -> {
                            InviteCreateSpec inviteSpec = InviteCreateSpec.builder()
                                    .maxAge(0) // Convite permanente
                                    .maxUses(0) // Usos ilimitados
                                    .unique(true)
                                    .build();

                            return voiceChannel.createInvite(inviteSpec)
                                    .map(invite -> "https://discord.gg/" + invite.getCode());
                        })
                );
    }

    private Mono<CategoryEditMono> createCategoryWithRole(String categoryName, Guild guild, RoleEditMono role) {
        return findEveryoneRole(guild)
                .flatMap(everyoneRole -> guild.createCategory(categoryName)
                        .withPermissionOverwrites(getCategoryPermissionOverwrites(everyoneRole, role))
                        .flatMap(category -> Mono.just(category.edit()))
                );
    }

    private Mono<RoleEditMono> createRole(String className, Guild guild) {
        String roleName = className;
        return guild.createRole()
                .withName(roleName)
                .map(role -> role.edit());
    }

    public Mono<DiscordOperationResult> createStudyGroupServer(StudyGroup studyGroup, StudyGroupCreateDto dto) {
        return DiscordClient.create(token)
                .login()
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> {
                    String className = generateClassName(studyGroup, dto);

                    return createRole(className, guild)
                            .flatMap(role -> {
                                long discordId = role.role().getId().asLong();

                                return createCategoryWithRole(className, guild, role)
                                        .flatMap(category ->
                                                createChannelsInCategory(className, guild, category, role)
                                                        .map(inviteUrl -> new DiscordOperationResult(discordId, inviteUrl))
                                        );
                            });
                });
    }

    public void registerUserToRole(Long userDiscordId, Long groupDiscordId) {

        registerOnGuild(userDiscordId, groupDiscordId)
                .doOnSuccess(success -> {
                    //log.info("user : " + userDiscordId.toString());
                    //log.info("group : " + groupDiscordId.toString());
                    log.info("registerUserToRole: se escreveu true, funcionou! {} ", success);
                })
                .doOnError(error -> {
                    log.info(error.getMessage());
                })
                .subscribe();
    }

    private Mono<Boolean> registerOnGuild(Long userDiscordId, Long groupDiscordId) {
        DiscordClient client = DiscordClient.create(token);

        return client.login()
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> addRoleToUser(guild, userDiscordId, groupDiscordId))
                .onErrorReturn(false)
                .doOnError(error -> log.info(error.getMessage()));
    }

    private Mono<Boolean> addRoleToUser(Guild guild, Long userDiscordId, Long groupDiscordId) {
        Snowflake userSnowflake = Snowflake.of(userDiscordId);
        Snowflake roleSnowflake = Snowflake.of(groupDiscordId);

        return guild.getMemberById(userSnowflake)
                .flatMap(member -> member.addRole(roleSnowflake))
                .thenReturn(true)
                .onErrorReturn(false)
                .doOnError(error -> log.info(error.getMessage()));
    }

    public void removeUserFromRole(Long userDiscordId, Long groupDiscordId) {

        removeOnGuild(userDiscordId, groupDiscordId)
                .doOnSuccess(success -> {
                    //log.info("user : {}", userDiscordId.toString());
                    //log.info("group : {}", groupDiscordId.toString());
                    log.info("removeUserFromRole: se escreveu true, funcionou! {}", success);
                })
                .doOnError(error -> {
                    log.info(error.getMessage());
                })
                .subscribe();
    }

    private Mono<Boolean> removeOnGuild(Long userDiscordId, Long groupDiscordId) {
        DiscordClient client = DiscordClient.builder(token)
                .build();

        return client.gateway()
                .setEnabledIntents(IntentSet.all())
                .login()
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> removeRoleFromUser(guild, userDiscordId, groupDiscordId))
                .onErrorReturn(false);
    }

    private Mono<Boolean> removeRoleFromUser(Guild guild, Long userDiscordId, Long groupDiscordId) {
        Snowflake userSnowflake = Snowflake.of(userDiscordId);
        Snowflake roleSnowflake = Snowflake.of(groupDiscordId);

        return guild.getMemberById(userSnowflake)
                .flatMap(member -> member.removeRole(roleSnowflake))
                .then(Mono.defer(() -> guild.getRoleById(roleSnowflake)
                        .flatMap(role -> checkIfRoleIsEmpty(guild, roleSnowflake)
                                .flatMap(isEmpty -> {
                                    if (isEmpty) {
                                        log.info("role está vazia! {}", role.getName());
                                        return deleteAllResources(guild, role.getName())
                                                .then(role.delete())
                                                .thenReturn(true);
                                    }
                                    return Mono.just(true);
                                })
                        )))
                .onErrorResume(e -> {
                    log.error("Error in removeRoleFromUser: ", e);
                    return Mono.just(false);
                });
    }

    private Mono<Boolean> checkIfRoleIsEmpty(Guild guild, Snowflake roleId) {
        return guild.getMembers()
                .filter(member -> member.getRoleIds().contains(roleId))
                .next()
                .map(__ -> false) // Se encontrou algum membro, não está vazia
                .defaultIfEmpty(true); // Se não encontrou, está vazia
    }
    
    private Mono<Void> deleteAllResources(Guild guild, String resourceName) {

        return guild.getChannels()
                .ofType(VoiceChannel.class)
                .filter(voiceChannel -> voiceChannel.getName().equalsIgnoreCase(resourceName))
                .next()
                .flatMap(voiceChannel ->
                        // Primeiro deleta todos os invites do canal de voz
                        voiceChannel.getInvites()
                                .flatMap(invite -> invite.delete("Removendo convite ao deletar recursos"))
                                .then()
                                // Depois deleta o canal
                                .then(voiceChannel.delete())
                )
                .then()
                // Deleta canal de texto
                .then(Mono.defer(() ->
                        guild.getChannels()
                                .ofType(TextChannel.class)
                                .filter(textChannel -> textChannel.getName().equalsIgnoreCase(resourceName))
                                .next()
                                .flatMap(textChannel -> textChannel.delete())
                ))
                .then()
                // Deleta categoria
                .then(Mono.defer(() ->
                        guild.getChannels()
                                .ofType(Category.class)
                                .filter(category -> category.getName().equalsIgnoreCase(resourceName))
                                .next()
                                .flatMap(category -> category.delete())
                ))
                .then()
                .doOnSuccess(__ -> log.info("Todos recursos deletados para: {}", resourceName))
                .doOnError(e -> log.error("Erro ao deletar recursos", e));
    }


    public String createInviteGuild() {
        return DiscordClient.create(token)
                .login()
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> guild.getChannelById(Snowflake.of("1237585539750826046")))
                .ofType(TextChannel.class) // Filtra apenas TextChannel (que suporta createInvite)
                .flatMap(textChannel -> {
                    InviteCreateSpec inviteSpec = InviteCreateSpec.builder()
                            .maxAge(0) // Convite permanente
                            .maxUses(0) // Usos ilimitados
                            .unique(true)
                            .build();

                    return textChannel.createInvite(inviteSpec)
                            .map(invite -> "https://discord.gg/" + invite.getCode());
                }).block();
    }

}