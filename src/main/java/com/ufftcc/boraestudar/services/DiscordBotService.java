package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.entities.StudyGroup;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.*;
import discord4j.rest.util.PermissionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static discord4j.rest.util.Permission.VIEW_CHANNEL;
import static java.lang.System.out;


@Service
public class DiscordBotService {

    private static final Logger log = LoggerFactory.getLogger(DiscordBotService.class);
    @Value("${discord.token}")
    String token;

    @Value("${discord.guildId}")
    String guildId;

    private void createChannelInCategory(Integer typeChannel, String textChannelName, Guild guild, CategoryEditMono category) {
        out.println("Criando o Canal tipo " + typeChannel.toString() + " . . .");
        if (typeChannel.equals(1)) {
            createTextChannelInCategory(textChannelName, guild, category);
        }
        if (typeChannel.equals(2)) {
            createVoiceChannelInCategory(textChannelName, guild, category);
        }
    }

    private void createTextChannelInCategory(String textChannelName, Guild guild, CategoryEditMono category) {
        out.println("Criando o Canal de Texto . . .");
        TextChannelCreateMono textChannel = guild.createTextChannel(textChannelName);
        out.println("Associando o Canal de Texto: " + textChannelName + " à Categoria: " + category.category().getName());
        textChannel.withParentId(category.category().getId()).block();
        out.println("Canal de Texto criado com sucesso: " + textChannelName);
        out.println("id do Canal de Texto: " + textChannel);

    }

    private void createVoiceChannelInCategory(String voiceChannelName, Guild guild, CategoryEditMono category) {
        out.println("Criando o Canal de Voz . . .");
        VoiceChannelCreateMono voiceChannel = guild.createVoiceChannel(voiceChannelName);
        out.println("Associando o Canal de Voz: " + voiceChannelName + " à Categoria: " + category.category().getName());
        voiceChannel.withParentId(category.category().getId()).block();
        out.println("Canal de Voz criado com sucesso: " + voiceChannelName);
        out.println("id do Canal de Voz: " + voiceChannel);
    }

    private CategoryEditMono createCategoryWithRole(String categoryName, Guild guild, RoleEditMono role) {
        out.println("Criando a Categoria . . .");
        PermissionSet viewCategory = PermissionSet.of(VIEW_CHANNEL);
        PermissionSet disableCategory = PermissionSet.none();
        disableCategory.removeAll(viewCategory);
        Flux<Role> fluxRoles = guild.getRoles();
        List<Role> listRoles = fluxRoles.collectList().block();
        Snowflake idEveryone = null;
        for (Role iteratorRole : listRoles) {
            if (iteratorRole.getName().equals("@everyone")) {
                idEveryone = iteratorRole.getId();
                out.println("id do Cargo @everyone: " + idEveryone);
            }
        }
        out.println("id do Cargo parâmetro: " + role.role().getId());

        CategoryCreateMono createCategory = guild
                .createCategory(categoryName)
                .withPermissionOverwrites(PermissionOverwrite
                                .forRole(idEveryone, disableCategory, viewCategory),
                        PermissionOverwrite
                                .forRole(role.role().getId(), viewCategory, disableCategory));

        List<PermissionOverwrite> po = createCategory.permissionOverwrites().get();

        for (PermissionOverwrite x : po) {
            out.println("PermissionOverwrite x: " + x.toString());
        }

        CategoryEditMono category = createCategory.block().edit();

        out.println("Categoria criada com sucesso: " + categoryName);
        out.println("id da Categoria: " + category.category().getId());

        return category;
    }

    private RoleEditMono createRole(String classCode, String className, Guild guild) {
        out.println("Criando o Cargo. . .");
        String roleName = classCode + "-" + className;
        out.println("Nome: " + roleName);
        RoleCreateMono role = guild.createRole().withName(roleName);
        return role.block().edit();
    }


        public void createStudyGroupServer2(StudyGroup studyGroup, StudyGroupCreateDto dto) {
            DiscordClient client = DiscordClient.create(token);

            client.withGateway((GatewayDiscordClient gateway) -> {
                assert gateway != null;
                Guild guild = gateway.getGuildById(Snowflake.of(guildId)).block();
                assert guild != null;

                //List<Role> roleList = guild.getRoles().collectList().block();

                String randomAsString = String.valueOf(studyGroup.getId());

                final String classCode = dto.getSubject().getCode();
                final String className = dto.getSubject().getName().concat("-" + randomAsString);

                RoleEditMono role = createRole(classCode, className, guild);

                long discordId  = Objects.requireNonNull(role.block()).getId().asLong();

                log.info("THIAGO: " + String.valueOf(discordId));

                CategoryEditMono category = createCategoryWithRole(classCode + "-" + className, guild, role);
                createChannelInCategory(1, classCode + "-" + className, guild, category);
                createChannelInCategory(2, classCode + "-" + className, guild, category);

                return Mono.empty();

            }).then().subscribe();

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





//    public Mono<Long> createStudyGroupServer(StudyGroup studyGroup, StudyGroupCreateDto dto) {
//        DiscordClient client = DiscordClient.create(token);
//
//        AtomicReference<Long> discordId = new AtomicReference<>(0L);
//
//        client.withGateway((GatewayDiscordClient gateway) -> {
//            assert gateway != null;
//            Guild guild = gateway.getGuildById(Snowflake.of(guildId)).block();
//            assert guild != null;
//
//            String randomAsString = String.valueOf(studyGroup.getId());
//
//            final String classCode = dto.getSubject().getCode();
//            final String className = dto.getSubject().getName().concat("-" + randomAsString);
//
//            //RoleEditMono role = createRole(classCode, className, guild);
//
//            createRole(classCode, className, guild).flatMap(role -> {
//                //studyGroup.setDiscordId(role.getId().asLong());
//                CategoryEditMono category = createCategoryWithRole(classCode + "-" + className, guild, role.edit());
//                createChannelInCategory(1, classCode + "-" + className, guild, category);
//                createChannelInCategory(2, classCode + "-" + className, guild, category);
//                discordId.set(role.getId().asLong());
//                return Mono.empty();
//            });
//
//            return Mono.empty();
//
//        }).then();
//
//        return Mono.just(discordId.get());
//    }

}
