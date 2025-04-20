package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.entities.StudyGroup;

import static discord4j.rest.util.Permission.VIEW_CHANNEL;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.*;
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
        log.info("Criando o Canal tipo {}  . . .", typeChannel.toString());
        if (typeChannel.equals(1)) {
            createTextChannelInCategory(textChannelName, guild, category);
        }
        if (typeChannel.equals(2)) {
            createVoiceChannelInCategory(textChannelName, guild, category);
        }
    }

    private void createTextChannelInCategory(String textChannelName, Guild guild, CategoryEditMono category) {
        log.info("Criando o Canal de Texto . . .");
        TextChannelCreateMono textChannel = guild.createTextChannel(textChannelName);
        log.info("Associando o Canal de Texto: {} à Categoria: {}", textChannelName, category.category().getName());
        textChannel.withParentId(category.category().getId()).block();
        log.info("Canal de Texto criado com sucesso: {}", textChannelName);
        log.info("id do Canal de Texto: {}", textChannel);
    }

    private void createVoiceChannelInCategory(String voiceChannelName, Guild guild, CategoryEditMono category) {
        log.info("Criando o Canal de Voz . . .");
        VoiceChannelCreateMono voiceChannel = guild.createVoiceChannel(voiceChannelName);
        log.info("Associando o Canal de Voz: {} à Categoria: {}",voiceChannelName,category.category().getName());
        voiceChannel.withParentId(category.category().getId()).block();
        log.info("Canal de Voz criado com sucesso: {}",voiceChannelName);
        log.info("id do Canal de Voz: {}",voiceChannel);
    }

    private CategoryEditMono createCategoryWithRole(String categoryName, Guild guild, RoleEditMono role) {
        log.info("Criando a Categoria . . .");
        PermissionSet viewCategory = PermissionSet.of(VIEW_CHANNEL);
        PermissionSet disableCategory = PermissionSet.none();
        disableCategory.removeAll(viewCategory);
        Flux<Role> fluxRoles = guild.getRoles();
        List<Role> listRoles = fluxRoles.collectList().block();
        Snowflake idEveryone = null;
        for (Role iteratorRole : listRoles) {
            if (iteratorRole.getName().equals("@everyone")) {
                idEveryone = iteratorRole.getId();
                log.info("id do Cargo @everyone: {}", idEveryone);
            }
        }
        log.info("id do Cargo parâmetro: {}", role.role().getId());

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

        log.info("Categoria criada com sucesso: {}", categoryName);
        log.info("id da Categoria: {}", category.category().getId());
        return category;
    }

    private RoleEditMono createRole(String classCode, String className, Guild guild) {
        log.info("Criando o Cargo. . .");
        String roleName = classCode + "-" + className;
        log.info("Nome: {}", roleName);
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
                                log.info("THIAGO: " + discordId);

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
                    log.info("user : " + userDiscordId.toString());
                    log.info("group : " + groupDiscordId.toString());
                    log.info("se escreveu true, funcionou! " + success);
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
                    log.info("user : {}", userDiscordId.toString());
                    log.info("group : {}", groupDiscordId.toString());
                    log.info("se escreveu true abaixo, funcionou! ");
                    log.info(success.toString());
                })
                .doOnError(error -> {
                    log.info(error.getMessage());
                })
                .subscribe();
    }

    private Mono<Boolean> removeOnGuild(Long userDiscordId, Long groupDiscordId) {
        DiscordClient client = DiscordClient.create(token);

        return client.login()
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> removeRoleFromUser(guild, userDiscordId, groupDiscordId))
                .onErrorReturn(false);
    }

    private Mono<Boolean> removeRoleFromUser(Guild guild, Long userDiscordId, Long groupDiscordId) {
        Snowflake userSnowflake = Snowflake.of(userDiscordId);
        Snowflake roleSnowflake = Snowflake.of(groupDiscordId);

        return guild.getMemberById(userSnowflake)
                .flatMap(member -> member.removeRole(roleSnowflake))
                .thenReturn(true)
                .onErrorReturn(false);
    }

    /**
     * Método para excluir uma role e todos os canais e categorias associados.
     * @param roleSnowflake O snowflake da role a ser excluída.
     */
    public void deleteRoleAndAssociatedChannels(String roleSnowflake) {
        DiscordClient client = DiscordClient.create(token);

        // Conectar e obter a guilda
        client.login()
                .flatMap(gateway -> gateway.getGuildById(Snowflake.of(guildId)))
                .flatMap(guild -> {
                    // Obter a role a partir do snowflake
                    return guild.getRoleById(Snowflake.of(roleSnowflake))
                            .flatMap(role -> {
                                // Excluir todos os canais associados à role
                                return deleteChannels(guild, role)
                                        .then(deleteRole(guild, role));
                            });
                })
                .doOnError(error -> log.error("Erro ao excluir role e canais: ", error))
                .subscribe();
    }

    /**
     * Método para excluir os canais associados à role.
     * @param guild O servidor (guild).
     * @param role A role que será usada para filtrar os canais associados.
     * @return Mono<Void> após excluir os canais.
     */
    private Mono<Void> deleteChannels(Guild guild, Role role) {
        return guild.getChannels()
                .filter(channel -> channel instanceof TextChannel || channel instanceof VoiceChannel)
                .filter(channel -> channel.getPermissionsFor(role).contains(Permission.MANAGE_CHANNELS))
                .flatMap(channel -> {
                    log.info("Excluindo o canal: {}", channel.getName());
                    return channel.delete();
                })
                .then();
    }

    /**
     * Método para excluir a role.
     * @param guild O servidor (guild).
     * @param role A role a ser excluída.
     * @return Mono<Void> após excluir a role.
     */
    private Mono<Void> deleteRole(Guild guild, Role role) {
        log.info("Excluindo a role: {}", role.getName());
        return role.delete();
    }


}