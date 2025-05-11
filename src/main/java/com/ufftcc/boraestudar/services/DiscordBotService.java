package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.entities.StudyGroup;

import static discord4j.rest.util.Permission.VIEW_CHANNEL;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.*;
import discord4j.core.spec.*;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DiscordBotService {

    private static final Logger log = LoggerFactory.getLogger(DiscordBotService.class);
    @Value("${discord.token}")
    String token;
    @Value("${discord.guildId}")
    String guildId;

    private void createChannelInCategory(Integer typeChannel, String textChannelName, Guild guild, CategoryEditMono category) {
        //log.info("Criando o Canal tipo {}  . . .", typeChannel.toString());
        if (typeChannel.equals(1)) {
            createTextChannelInCategory(textChannelName, guild, category);
        }
        if (typeChannel.equals(2)) {
            createVoiceChannelInCategory(textChannelName, guild, category);
        }
    }

    private void createTextChannelInCategory(String textChannelName, Guild guild, CategoryEditMono category) {
        //log.info("Criando o Canal de Texto . . .");
        TextChannelCreateMono textChannel = guild.createTextChannel(textChannelName);
        //log.info("Associando o Canal de Texto: {} à Categoria: {}", textChannelName, category.category().getName());
        textChannel.withParentId(category.category().getId()).block();
        //log.info("Canal de Texto criado com sucesso: {}", textChannelName);
        //log.info("id do Canal de Texto: {}", textChannel);
    }

    private void createVoiceChannelInCategory(String voiceChannelName, Guild guild, CategoryEditMono category) {
        //log.info("Criando o Canal de Voz . . .");
        VoiceChannelCreateMono voiceChannel = guild.createVoiceChannel(voiceChannelName);
        //log.info("Associando o Canal de Voz: {} à Categoria: {}",voiceChannelName,category.category().getName());
        voiceChannel.withParentId(category.category().getId()).block();
        //log.info("Canal de Voz criado com sucesso: {}",voiceChannelName);
        //log.info("id do Canal de Voz: {}",voiceChannel);
    }

    private CategoryEditMono createCategoryWithRole(String categoryName, Guild guild, RoleEditMono role) {
        //log.info("Criando a Categoria . . .");
        PermissionSet viewCategory = PermissionSet.of(VIEW_CHANNEL);
        PermissionSet disableCategory = PermissionSet.none();
        disableCategory.removeAll(viewCategory);
        Flux<Role> fluxRoles = guild.getRoles();
        List<Role> listRoles = fluxRoles.collectList().block();
        Snowflake idEveryone = null;
        for (Role iteratorRole : listRoles) {
            if (iteratorRole.getName().equals("@everyone")) {
                idEveryone = iteratorRole.getId();
                //log.info("id do Cargo @everyone: {}", idEveryone);
            }
        }
        //log.info("id do Cargo parâmetro: {}", role.role().getId());

        CategoryCreateMono createCategory = guild
                .createCategory(categoryName)
                .withPermissionOverwrites(PermissionOverwrite
                                .forRole(idEveryone, disableCategory, viewCategory),
                        PermissionOverwrite
                                .forRole(role.role().getId(), viewCategory, disableCategory));

        List<PermissionOverwrite> po = createCategory.permissionOverwrites().get();

        for (PermissionOverwrite x : po) {
            log.info("PermissionOverwrite x: {}", x.toString());
        }

        CategoryEditMono category = createCategory.block().edit();

        //log.info("Categoria criada com sucesso: {}", categoryName);
        //log.info("id da Categoria: {}", category.category().getId());
        return category;
    }

    private RoleEditMono createRole(String classCode, String className, Guild guild) {
        //log.info("Criando o Cargo. . .");
        String roleName = classCode + "-" + className;
        //log.info("Nome: {}", roleName);
        RoleCreateMono role = guild.createRole().withName(roleName);
        return role.block().edit();
    }

    public Mono<Long> createStudyGroupServer(StudyGroup studyGroup, StudyGroupCreateDto dto) {
        DiscordClient client = DiscordClient.create(token);

        return client.login() // Retorna Mono<GatewayDiscordClient>
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> {
                    String randomAsString = String.valueOf(studyGroup.getId());
                    String classCode = dto.getSubject().getCode();
                    String className = dto.getSubject().getName().concat("-" + randomAsString);

                    return createRole(classCode, className, guild)
                            .flatMap(role -> {
                                long discordId = role.getId().asLong();
                                //log.info("discordId : {}",  discordId);

                                return createCategoryWithRole(classCode + "-" + className, guild, role.edit())
                                        .flatMap(category ->
                                                Mono.when(
                                                        Mono.fromRunnable(() -> createChannelInCategory(1, classCode + "-" + className, guild, category.edit())),
                                                        Mono.fromRunnable(() -> createChannelInCategory(2, classCode + "-" + className, guild, category.edit()))
                                                )
                                        )
                                        .thenReturn(discordId); // Retorna o discordId corretamente
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

        String voiceChannelName = resourceName;
        String textChannelName = resourceName.replace(" ","-").toLowerCase();
        String categoryName = resourceName;

        // deletando canal de texto
        return guild.getChannels()
                .ofType(TextChannel.class)
                .filter(textChannel -> textChannel.getName().equals(textChannelName))
                .flatMap(textChannel -> textChannel.delete())
                .then()
                // deletando canal de voz
                .then(Mono.defer(() ->
                        guild.getChannels()
                                .ofType(VoiceChannel.class)
                                .filter(voiceChannel -> voiceChannel.getName().equals(voiceChannelName))
                                .next()
                                .flatMap(voiceChannel -> voiceChannel.delete())
                 ))
                .then()
                //deletando categoria
                .then(Mono.defer(() ->
                        guild.getChannels()
                                .ofType(Category.class)
                                .filter(category -> category.getName().equals(categoryName))
                                .next()
                                .flatMap(category -> category.delete())
                ))
                .then();

    }

}